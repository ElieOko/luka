package elieoko.mobile.luka.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import elieoko.mobile.luka.core.CrashReporter
import elieoko.mobile.luka.core.PhoneNumbers
import elieoko.mobile.luka.core.PushNotifier
import elieoko.mobile.luka.data.mapper.mergeInto
import elieoko.mobile.luka.data.mapper.toAuthTokens
import elieoko.mobile.luka.data.remote.LukaApi
import elieoko.mobile.luka.data.remote.TokenStore
import elieoko.mobile.luka.domain.model.AuthChannel
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.OtpChallenge
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.model.UserSession
import elieoko.mobile.luka.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SessionRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val api: LukaApi,
    private val tokenStore: TokenStore,
    private val pushNotifier: PushNotifier,
    private val crashReporter: CrashReporter,
) : SessionRepository {

    override val session: Flow<UserSession?> = dataStore.data.map { prefs ->
        prefs.toSession().also { current ->
            tokenStore.set(current?.token, current?.refreshToken)
        }
    }

    override suspend fun current(): UserSession? = session.first()

    override suspend fun requestOtp(identifier: AuthIdentifier, newAccount: Boolean): OtpChallenge {
        check(identifier.channel == AuthChannel.PHONE) { "Indique un numéro congolais." }
        val phone = PhoneNumbers.normalize(identifier.value)
        crashReporter.breadcrumb("otp_requested:PHONE")
        if (newAccount) api.registerPhone(phone) else api.requestLoginOtp(phone)
        dataStore.edit { prefs ->
            prefs[Keys.pendingValue] = phone
            prefs[Keys.pendingChannel] = AuthChannel.PHONE.name
            prefs[Keys.pendingNewAccount] = newAccount
        }
        return OtpChallenge(AuthIdentifier(AuthChannel.PHONE, phone))
    }

    override suspend fun resendOtp(identifier: AuthIdentifier) {
        val phone = PhoneNumbers.normalize(identifier.value)
        val newAccount = dataStore.data.first()[Keys.pendingNewAccount] ?: false
        if (newAccount) api.resendOtp(phone) else api.requestLoginOtp(phone)
        crashReporter.breadcrumb("otp_resent")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun verifyOtp(identifier: AuthIdentifier, code: String): UserSession {
        val phone = PhoneNumbers.normalize(identifier.value)
        val newAccount = dataStore.data.first()[Keys.pendingNewAccount] ?: false
        val payload = if (newAccount) api.verifyOtp(phone, code) else api.verifyLoginOtp(phone, code)
        val tokens = payload.data.toAuthTokens()
        tokenStore.set(tokens.accessToken, tokens.refreshToken)
        val existing = current()
        val profile = tokens.user.mergeInto(
            identifier = AuthIdentifier(AuthChannel.PHONE, tokens.user.phone ?: phone),
            existing = existing?.profile,
        ).copy(
            id = tokens.user.userId?.toString() ?: existing?.profile?.id ?: Uuid.random().toString(),
            welcomeSeen = true,
        )
        val session = UserSession(
            token = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            profile = profile,
        )
        persist(session)
        pushNotifier.login(profile.id)
        hydrateRemoteProfile()
        crashReporter.breadcrumb("session_persisted")
        return current() ?: session
    }

    override suspend fun refreshRemoteProfile() = hydrateRemoteProfile()

    override suspend fun markWelcomeSeen() = update { it.copy(welcomeSeen = true) }

    override suspend fun saveProfession(professionId: String, domainId: Long?) {
        update { it.copy(profession = Profession.fromId(professionId), domainId = domainId) }
        if (domainId != null) {
            runCatching { api.savePreferences(listOf(domainId)) }
                .onFailure { crashReporter.capture(it) }
        }
    }

    override suspend fun saveLocation(countryCode: String, regionId: String, cityName: String?) {
        update { it.copy(countryCode = countryCode, regionId = regionId, cityName = cityName) }
        val profile = current()?.profile ?: return
        runCatching {
            api.updateProfile(
                fullName = profile.displayName.takeIf { it.isNotBlank() },
                email = profile.email.takeIf { it.contains("@") },
                city = cityName,
                country = countryCode,
            )
        }.onSuccess { dto ->
            update { current -> dto.mergeInto(current.identifier, current) }
        }.onFailure { crashReporter.capture(it) }
    }

    override suspend fun markAnalysisLaunched() = update { it.copy(analysisLaunched = true) }

    override suspend fun updateProfile(displayName: String, bio: String, email: String, cityName: String?) {
        val profile = current()?.profile ?: return
        val resolvedCity = cityName ?: profile.cityName
        update {
            it.copy(
                displayName = displayName,
                bio = bio,
                email = email.ifBlank { it.email },
                cityName = resolvedCity,
                countryCode = it.countryCode ?: "CD",
                regionId = resolvedCity?.let { name -> CongoCatalog.regionIdFor(name, null) } ?: it.regionId,
            )
        }
        val latest = current()?.profile ?: return
        val mail = latest.email
        val city = latest.cityName
        val country = latest.countryCode ?: "CD"
        if (!latest.profileCompleted && mail.contains("@")) {
            runCatching { api.completeProfile(displayName, mail) }
                .onSuccess { dto -> update { current -> dto.mergeInto(current.identifier, current) } }
                .onFailure { crashReporter.capture(it) }
        }
        val updated = api.updateProfile(
            fullName = displayName,
            email = mail.takeIf { it.contains("@") },
            city = city,
            country = country,
        )
        update { current -> updated.mergeInto(current.identifier, current).copy(bio = bio) }
    }

    override suspend fun selectPlan(planId: String, extraProfessionIds: List<String>) = update {
        it.copy(
            planId = planId,
            extraProfessionIds = extraProfessionIds,
            visibleToRecruiters = LukaPlans.isProfessional(planId),
        )
    }

    override suspend fun saveCv(fileName: String, mimeType: String) = update {
        it.copy(cvFileName = fileName, cvMime = mimeType)
    }

    override suspend fun resetDemo() {
        pushNotifier.logout()
        tokenStore.clear()
        dataStore.edit { it.clear() }
    }

    private suspend fun update(transform: (UserProfile) -> UserProfile) {
        val current = current() ?: return
        persist(current.copy(profile = transform(current.profile)))
    }

    private suspend fun persist(session: UserSession) {
        val profile = session.profile
        tokenStore.set(session.token, session.refreshToken)
        dataStore.edit { prefs ->
            prefs[Keys.token] = session.token
            prefs[Keys.refresh] = session.refreshToken
            prefs[Keys.userId] = profile.id
            prefs[Keys.displayName] = profile.displayName
            prefs[Keys.bio] = profile.bio
            prefs[Keys.photo] = profile.photoUrl
            prefs[Keys.channel] = profile.identifier.channel.name
            prefs[Keys.value] = profile.identifier.value
            prefs[Keys.profession] = profile.profession?.id.orEmpty()
            prefs[Keys.country] = profile.countryCode.orEmpty()
            prefs[Keys.region] = profile.regionId.orEmpty()
            prefs[Keys.plan] = profile.planId
            prefs[Keys.extras] = profile.extraProfessionIds.joinToString(",")
            prefs[Keys.analysis] = profile.analysisLaunched
            prefs[Keys.welcome] = profile.welcomeSeen
            prefs[Keys.visible] = profile.visibleToRecruiters
            prefs[Keys.cvName] = profile.cvFileName
            prefs[Keys.cvMime] = profile.cvMime
            prefs[Keys.email] = profile.email
            prefs[Keys.cityName] = profile.cityName.orEmpty()
            if (profile.domainId != null) prefs[Keys.domainId] = profile.domainId else prefs.remove(Keys.domainId)
            prefs[Keys.profileCompleted] = profile.profileCompleted
            prefs[Keys.isCertified] = profile.isCertified
        }
    }

    private fun Preferences.toSession(): UserSession? {
        val token = this[Keys.token].orEmpty()
        if (token.isBlank()) return null
        val channel = AuthChannel.valueOf(this[Keys.channel] ?: AuthChannel.PHONE.name)
        val value = this[Keys.value].orEmpty()
        val professionId = this[Keys.profession].orEmpty()
        return UserSession(
            token = token,
            refreshToken = this[Keys.refresh].orEmpty(),
            profile = UserProfile(
                id = this[Keys.userId].orEmpty(),
                displayName = this[Keys.displayName].orEmpty(),
                bio = this[Keys.bio].orEmpty(),
                photoUrl = this[Keys.photo].orEmpty(),
                identifier = AuthIdentifier(channel, value),
                profession = professionId.takeIf { it.isNotBlank() }?.let(Profession::fromId),
                countryCode = this[Keys.country]?.takeIf { it.isNotBlank() },
                regionId = this[Keys.region]?.takeIf { it.isNotBlank() },
                planId = this[Keys.plan] ?: "starter",
                extraProfessionIds = this[Keys.extras].orEmpty().split(",").filter { it.isNotBlank() },
                analysisLaunched = this[Keys.analysis] ?: false,
                welcomeSeen = this[Keys.welcome] ?: false,
                visibleToRecruiters = this[Keys.visible] ?: false,
                cvFileName = this[Keys.cvName].orEmpty(),
                cvMime = this[Keys.cvMime].orEmpty(),
                email = this[Keys.email].orEmpty(),
                cityName = this[Keys.cityName]?.takeIf { it.isNotBlank() },
                domainId = this[Keys.domainId],
                profileCompleted = this[Keys.profileCompleted] ?: false,
                isCertified = this[Keys.isCertified] ?: false,
            ),
        )
    }

    private suspend fun hydrateRemoteProfile() {
        if (tokenStore.accessToken.isNullOrBlank()) return
        runCatching { api.getProfile() }
            .onSuccess { dto -> update { current -> dto.mergeInto(current.identifier, current) } }
            .onFailure { crashReporter.capture(it) }
        runCatching { api.getPreferences() }
            .onSuccess { prefs ->
                val domainId = prefs.domainIds.firstOrNull()
                if (domainId != null) update { it.copy(domainId = domainId) }
            }
            .onFailure { crashReporter.capture(it) }
    }

    private object Keys {
        val token = stringPreferencesKey("token")
        val refresh = stringPreferencesKey("refresh")
        val userId = stringPreferencesKey("userId")
        val displayName = stringPreferencesKey("displayName")
        val bio = stringPreferencesKey("bio")
        val photo = stringPreferencesKey("photo")
        val channel = stringPreferencesKey("channel")
        val value = stringPreferencesKey("value")
        val profession = stringPreferencesKey("profession")
        val country = stringPreferencesKey("country")
        val region = stringPreferencesKey("region")
        val plan = stringPreferencesKey("plan")
        val extras = stringPreferencesKey("extras")
        val analysis = booleanPreferencesKey("analysis")
        val welcome = booleanPreferencesKey("welcome")
        val visible = booleanPreferencesKey("visible")
        val cvName = stringPreferencesKey("cvName")
        val cvMime = stringPreferencesKey("cvMime")
        val email = stringPreferencesKey("email")
        val cityName = stringPreferencesKey("cityName")
        val domainId = longPreferencesKey("domainId")
        val profileCompleted = booleanPreferencesKey("profileCompleted")
        val pendingValue = stringPreferencesKey("pendingValue")
        val pendingChannel = stringPreferencesKey("pendingChannel")
        val pendingNewAccount = booleanPreferencesKey("pendingNewAccount")
        val isCertified = booleanPreferencesKey("isCertified")
    }
}
