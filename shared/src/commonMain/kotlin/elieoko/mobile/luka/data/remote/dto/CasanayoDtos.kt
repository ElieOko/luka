package elieoko.mobile.luka.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ApiEnvelope<T>(
    val message: String = "",
    val data: T? = null,
)

@Serializable
data class ApiErrorBody(
    val message: String? = null,
    val detailMessage: String? = null,
    val error: String? = null,
    val status: Int? = null,
)

@Serializable
data class PhoneRegisterRequest(
    val phone: String,
)

@Serializable
data class VerifyRequest(
    val identifier: String,
    val code: String,
)

@Serializable
data class IdentifiantRequest(
    val identifier: String,
)

@Serializable
data class ProfileCompletionRequest(
    val fullName: String,
    val email: String,
)

@Serializable
data class UpdateProfileRequest(
    val fullName: String? = null,
    val email: String? = null,
    val city: String? = null,
    val country: String? = null,
)

@Serializable
data class SaveUserPreferencesRequest(
    val domainIds: List<Long>,
)

@Serializable
data class UserDto(
    val userId: Long? = null,
    val email: String? = null,
    val username: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val country: String? = null,
    val isPremium: Boolean = false,
    val isCertified: Boolean = false,
    val profileCompleted: Boolean = false,
    val certified: Boolean? = null,
)

@Serializable
data class UserPreferencesDto(
    val domainIds: List<Long> = emptyList(),
    val domains: List<SearchDomainDto> = emptyList(),
)

@Serializable
data class SearchDomainDto(
    val id: Long,
    val name: String,
    val searchAgent: Long? = null,
    val isActive: Boolean = true,
    val professions: List<DomainProfessionDto> = emptyList(),
)

@Serializable
data class DomainProfessionDto(
    val id: Long,
    val domainId: Long,
    val name: String,
    val isActive: Boolean = true,
)

@Serializable
data class CongoCityDto(
    val id: Long,
    val name: String,
    val province: String? = null,
    val isActive: Boolean = true,
)

@Serializable
data class JobOfferPageDto(
    val content: List<StoredJobOfferDto> = emptyList(),
    val page: Int = 0,
    val size: Int = 0,
    val totalElements: Long = 0,
)

@Serializable
data class StoredJobOfferDto(
    val id: Long,
    val searchAgent: Long? = null,
    val title: String,
    val employer: String,
    val country: String? = null,
    val city: String? = null,
    val province: String? = null,
    val opportunityType: String? = null,
    val contractType: String? = null,
    val skills: List<String> = emptyList(),
    val advertisementUrl: String = "",
    val applicationUrl: String? = null,
    val status: String? = null,
    val publicationDate: String? = null,
    val collectedAt: String? = null,
)

@Serializable
data class LooseEnvelope(
    val message: String = "",
    val data: JsonElement? = null,
)
