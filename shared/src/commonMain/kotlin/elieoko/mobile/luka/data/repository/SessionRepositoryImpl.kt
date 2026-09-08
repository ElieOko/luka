package elieoko.mobile.luka.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.CrashReporter
import elieoko.mobile.luka.core.PushNotifier
import elieoko.mobile.luka.domain.model.AuthChannel
import elieoko.mobile.luka.domain.model.AuthIdentifier
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
    private val config: AppConfig,
    private val pushNotifier: PushNotifier,
    private val crashReporter: CrashReporter,
) : SessionRepository {

    override val session: Flow<UserSession?> = dataStore.data.map { it.toSession() }

    override suspend fun current(): UserSession? = session.first()

    override suspend fun requestOtp(identifier: AuthIdentifier): OtpChallenge {
        crashReporter.breadcrumb("otp_requested:${identifier.channel}")
        dataStore.edit { prefs ->
            prefs[Keys.pendingValue] = identifier.value
            prefs[Keys.pendingChannel] = identifier.channel.name
        }
        return OtpChallenge(identifier)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun verifyOtp(identifier: AuthIdentifier, code: String): UserSession {
        check(code == config.demoOtpCode) { "Code incorrect. Pour la démo, utilise ${config.demoOtpCode}." }
        val existing = current()
        val profile = existing?.profile?.copy(identifier = identifier)
            ?: UserProfile(
                id = Uuid.random().toString(),
                displayName = defaultName(identifier),
                bio = "Je cherche mon prochain rôle en RDC.",
                photoUrl = "",
                identifier = identifier,
                profession = null,
                countryCode = null,
                regionId = null,
                planId = "starter",
                extraProfessionIds = emptyList(),
                analysisLaunched = false,
                welcomeSeen = true,
                visibleToRecruiters = false,
            )
        val session = UserSession(token = "luka_${profile.id}", profile = profile.copy(welcomeSeen = true))
        persist(session)
        pushNotifier.login(profile.id)
        crashReporter.breadcrumb("session_persisted")
        return session
    }

    override suspend fun markWelcomeSeen() = update { it.copy(welcomeSeen = true) }

    override suspend fun saveProfession(professionId: String) = update {
        it.copy(profession = Profession.fromId(professionId))
    }

    override suspend fun saveLocation(countryCode: String, regionId: String) = update {
        it.copy(countryCode = countryCode, regionId = regionId)
    }

    override suspend fun markAnalysisLaunched() = update { it.copy(analysisLaunched = true) }

    override suspend fun updateProfile(displayName: String, bio: String) = update {
        it.copy(displayName = displayName, bio = bio)
    }

    override suspend fun selectPlan(planId: String, extraProfessionIds: List<String>) = update {
        it.copy(
            planId = planId,
            extraProfessionIds = extraProfessionIds,
            visibleToRecruiters = planId == "pro" || planId == "elite",
        )
    }

    override suspend fun saveCv(fileName: String, mimeType: String) = update {
        it.copy(cvFileName = fileName, cvMime = mimeType)
    }

    override suspend fun resetDemo() {
        pushNotifier.logout()
        dataStore.edit { it.clear() }
    }

    private suspend fun update(transform: (UserProfile) -> UserProfile) {
        val current = current() ?: return
        persist(current.copy(profile = transform(current.profile)))
    }

    private suspend fun persist(session: UserSession) {
        val profile = session.profile
        dataStore.edit { prefs ->
            prefs[Keys.token] = session.token
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
            ),
        )
    }

    private fun defaultName(identifier: AuthIdentifier): String =
        if (identifier.channel == AuthChannel.EMAIL) {
            identifier.value.substringBefore("@").replaceFirstChar { it.uppercase() }
        } else {
            "Talent Luka"
        }

    private object Keys {
        val token = stringPreferencesKey("token")
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
        val pendingValue = stringPreferencesKey("pendingValue")
        val pendingChannel = stringPreferencesKey("pendingChannel")
    }
}
