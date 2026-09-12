package elieoko.mobile.luka.data.remote.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
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

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PhoneRegisterRequest(
    val phone: String,
    @EncodeDefault val isStudent: Boolean = false,
    val buildSerial: String? = null,
)

@Serializable
data class VerifyRequest(
    val identifier: String,
    val code: String,
    val buildSerial: String? = null,
)

@Serializable
data class IdentifiantRequest(
    val identifier: String,
    val buildSerial: String? = null,
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

@Serializable
data class AbonnementDto(
    val id: Long,
    val code: String = "",
    val name: String = "",
    val amountUsd: Double = 0.0,
    val description: String? = null,
    val active: Boolean = true,
)

@Serializable
data class DeviseDto(
    val id: Long,
    val code: String,
    val name: String,
    val tauxLocal: Double,
)

@Serializable
data class PaymentInitRequest(
    val abonnementId: Long,
    val devise: String,
    val phone: String,
)

@Serializable
data class FlexPaymentResponse(
    val code: String? = null,
    val message: String? = null,
    val orderNumber: String? = null,
    val url: String? = null,
    val paymentAccepted: Boolean = false,
)

@Serializable
data class PaiementDto(
    val id: Long? = null,
    val userId: Long? = null,
    val abonnementId: Long,
    val reference: String = "",
    val amount: String = "",
    val devise: String = "",
    val description: String? = null,
    val typePayment: String = "",
    val status: String = "",
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
