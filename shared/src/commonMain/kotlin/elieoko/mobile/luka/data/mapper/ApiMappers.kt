@file:OptIn(kotlin.time.ExperimentalTime::class)

package elieoko.mobile.luka.data.mapper

import elieoko.mobile.luka.data.remote.dto.CongoCityDto
import elieoko.mobile.luka.data.remote.dto.SearchDomainDto
import elieoko.mobile.luka.data.remote.dto.StoredJobOfferDto
import elieoko.mobile.luka.data.remote.dto.UserDto
import elieoko.mobile.luka.domain.model.AuthChannel
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.City
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.TradeChip
import elieoko.mobile.luka.domain.model.UserProfile
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlin.time.Instant

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto,
)

fun JsonElement?.toAuthTokens(): AuthTokens {
    val root = this as? JsonObject ?: error("Réponse de connexion inattendue.")
    val nested = root["profile"] as? JsonObject
    val access = root.str("accessToken", "token", "access_token")
        ?: nested?.str("accessToken", "token")
        ?: error("Jeton manquant. Réessaie le code.")
    val refresh = root.str("refreshToken", "refresh_token").orEmpty()
    val profileSource = nested ?: root
    val user = UserDto(
        userId = profileSource.long("userId", "id"),
        email = profileSource.str("email"),
        username = profileSource.str("username", "fullName", "name"),
        phone = profileSource.str("phone"),
        city = profileSource.str("city"),
        country = profileSource.str("country"),
        isPremium = profileSource.bool("isPremium") ?: false,
        isCertified = profileSource.bool("isCertified") ?: profileSource.bool("certified") ?: false,
        profileCompleted = profileSource.bool("profileCompleted") ?: false,
    )
    return AuthTokens(accessToken = access, refreshToken = refresh, user = user)
}

fun UserDto.mergeInto(
    identifier: AuthIdentifier,
    existing: UserProfile?,
): UserProfile {
    val phone = phone?.takeIf { it.isNotBlank() }
    val mail = email?.takeIf { it.isNotBlank() }
    val resolvedIdentifier = when {
        phone != null -> AuthIdentifier(AuthChannel.PHONE, phone)
        mail != null -> AuthIdentifier(AuthChannel.EMAIL, mail)
        else -> identifier
    }
    val fallbackName = existing?.displayName.orEmpty()
    val name = username?.takeIf { it.isNotBlank() }
        ?: fallbackName.takeIf { it.isNotBlank() }
        ?: defaultName(resolvedIdentifier)
    return (existing ?: UserProfile(
        id = "",
        displayName = name,
        bio = "Je cherche mon prochain rôle en RDC.",
        photoUrl = "",
        identifier = resolvedIdentifier,
        profession = null,
        countryCode = null,
        regionId = null,
        planId = "starter",
        extraProfessionIds = emptyList(),
        analysisLaunched = false,
        welcomeSeen = true,
        visibleToRecruiters = false,
    )).copy(
        id = userId?.toString() ?: existing?.id.orEmpty(),
        displayName = name,
        identifier = resolvedIdentifier,
        email = mail ?: existing?.email.orEmpty(),
        cityName = city?.takeIf { it.isNotBlank() } ?: existing?.cityName,
        countryCode = country?.takeIf { it.isNotBlank() } ?: existing?.countryCode,
        regionId = city?.takeIf { it.isNotBlank() }?.let { CongoCatalog.regionIdFor(it, null) } ?: existing?.regionId,
        profileCompleted = profileCompleted,
        planId = if (isPremium) LukaPlans.PROFESSIONAL else (existing?.planId ?: LukaPlans.STARTER),
        isCertified = isCertified || certified == true,
    )
}

fun CongoCityDto.toCity(): City = City(
    id = id.toString(),
    name = name,
    regionId = CongoCatalog.slug(province ?: name),
)

fun List<SearchDomainDto>.toTradeChips(): List<TradeChip> {
    val fromApi = filter { it.isActive }.flatMap { domain ->
        domain.professions.filter { it.isActive }.map { profession ->
            val mapped = Profession.fromCatalogName(profession.name)
            TradeChip(
                profession = mapped,
                title = profession.name,
                family = domain.name,
                tagline = mapped.tagline,
                domainId = domain.id,
            )
        }
    }
    val families = fromApi.map { it.family.lowercase() }.toSet()
    val rest = TradeChip.fromLocal().filter { it.family.lowercase() !in families }
    return fromApi + rest
}

fun StoredJobOfferDto.toJobOffer(): JobOffer {
    val cityName = city?.substringBefore(",")?.trim().orEmpty().ifBlank { "RDC" }
    val region = CongoCatalog.regionIdFor(city = cityName, province = province)
    val apply = applicationUrl?.takeIf { it.isNotBlank() } ?: advertisementUrl
    val remote = listOf(opportunityType, city, title).any { value ->
        value?.contains("télétravail", true) == true || value?.contains("remote", true) == true
    }
    return JobOffer(
        id = id.toString(),
        title = title,
        company = employer,
        companyLogoUrl = "",
        profession = inferProfession(title, skills, searchAgent),
        regionId = region,
        city = cityName,
        contract = contractType?.takeIf { it.isNotBlank() } ?: opportunityType.orEmpty().ifBlank { "Emploi" },
        salary = "",
        summary = skills.take(6).joinToString(" · ").ifBlank { verificationFallback() },
        applyUrl = apply,
        postedAtEpochMs = parseEpoch(publicationDate) ?: parseEpoch(collectedAt) ?: 0L,
        isRemote = remote,
    )
}

private fun StoredJobOfferDto.verificationFallback(): String =
    listOfNotNull(opportunityType, city).joinToString(" · ")

fun inferProfession(title: String, skills: List<String>, searchAgent: Long?): Profession {
    val text = (title + " " + skills.joinToString(" ")).lowercase()
    fun has(vararg keys: String) = keys.any { text.contains(it) }
    return when {
        has("cyber", "sécurité info", "securite", "soc", "pentest") -> Profession.CYBER_SECURITY
        has("data", "analyste", "machine learning", "analytics") -> Profession.DATA_AI
        has("ux", "ui/ux", "graphiste", "designer") -> Profession.DESIGN
        has("télécom", "telecom", "fibre", "réseau", "reseau", "wi-fi", "wifi") -> Profession.TELECOM
        has(
            "dévelop", "develop", "software", "program", "android", "kotlin", "java",
            "informatique", "assistant it", "systèmes it", "navision",
        ) -> Profession.SOFTWARE_ENGINEERING
        has("rh", "recrut", "talent", "ressources humaines") -> Profession.HUMAN_RESOURCES
        has("compta", "audit", "finance", "trésor", "tresor") -> Profession.FINANCE
        has("market", "communication", "marque") -> Profession.MARKETING
        has("commercial", "vente", "business develop") -> Profession.SALES
        has("manager", "chef de projet", "pilotage", "management", "assistant(e) au projet", "assistant au projet") -> Profession.MANAGEMENT
        has("électri", "electri") -> Profession.ELECTRICITY
        has("mine", "géolog", "geolog") -> Profession.MINING
        has("santé", "sante", "médic", "medic", "pharma", "infirm") -> Profession.HEALTH
        has("logist", "supply") -> Profession.LOGISTICS
        has("transport", "flotte") -> Profession.TRANSPORT
        has("enseignant", "éducation", "education", "formation") -> Profession.EDUCATION
        has("jurid", "avocat", "compliance") -> Profession.LEGAL
        has("agri") -> Profession.AGRICULTURE
        has("hôtel", "hotel", "restaur") -> Profession.HOSPITALITY
        searchAgent == 1L -> Profession.SOFTWARE_ENGINEERING
        searchAgent == 2L -> Profession.MANAGEMENT
        else -> Profession.OTHER
    }
}

private fun defaultName(identifier: AuthIdentifier): String =
    if (identifier.channel == AuthChannel.EMAIL) {
        identifier.value.substringBefore("@").replaceFirstChar { it.uppercase() }
    } else {
        "Talent Luka"
    }

private fun JsonObject.str(vararg keys: String): String? =
    keys.firstNotNullOfOrNull { key -> this[key]?.jsonPrimitive?.contentOrNull?.takeIf { it.isNotBlank() } }

private fun JsonObject.long(vararg keys: String): Long? =
    keys.firstNotNullOfOrNull { key -> this[key]?.jsonPrimitive?.longOrNull }

private fun JsonObject.bool(key: String): Boolean? = this[key]?.jsonPrimitive?.booleanOrNull

internal fun parseEpoch(value: String?): Long? {
    if (value.isNullOrBlank()) return null
    val date = value.take(10)
    runCatching {
        return LocalDate.parse(date).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }
    val instant = value.let { if (it.endsWith("Z") || it.contains("+")) it else "${it}Z" }
    return runCatching { Instant.parse(instant).toEpochMilliseconds() }.getOrNull()
}
