package elieoko.mobile.luka.data.remote

import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.DeviceSerial
import elieoko.mobile.luka.core.applyDeviceSerialHeaders
import elieoko.mobile.luka.data.remote.dto.AbonnementDto
import elieoko.mobile.luka.data.remote.dto.ApiEnvelope
import elieoko.mobile.luka.data.remote.dto.CongoCityDto
import elieoko.mobile.luka.data.remote.dto.DeviseDto
import elieoko.mobile.luka.data.remote.dto.FlexPaymentResponse
import elieoko.mobile.luka.data.remote.dto.IdentifiantRequest
import elieoko.mobile.luka.data.remote.dto.JobOfferPageDto
import elieoko.mobile.luka.data.remote.dto.LooseEnvelope
import elieoko.mobile.luka.data.remote.dto.PaiementDto
import elieoko.mobile.luka.data.remote.dto.PaymentInitRequest
import elieoko.mobile.luka.data.remote.dto.PhoneRegisterRequest
import elieoko.mobile.luka.data.remote.dto.ProfileCompletionRequest
import elieoko.mobile.luka.data.remote.dto.SaveUserPreferencesRequest
import elieoko.mobile.luka.data.remote.dto.UpdateProfileRequest
import elieoko.mobile.luka.data.remote.dto.SearchDomainDto
import elieoko.mobile.luka.data.remote.dto.UserDto
import elieoko.mobile.luka.data.remote.dto.UserPreferencesDto
import elieoko.mobile.luka.data.remote.dto.VerifyRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
import io.ktor.http.takeFrom
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LukaApi(
    private val http: HttpClient,
    private val config: AppConfig,
    private val tokenStore: TokenStore,
    private val json: Json,
    private val deviceSerial: DeviceSerial,
) {
    suspend fun registerPhone(phone: String, isStudent: Boolean = false): LooseEnvelope {
        val serial = deviceSerial.value()
        return send(HttpMethod.Post, "/api/v1/public/auth/register-phone", auth = false) {
            jsonBody(PhoneRegisterRequest(phone = phone, isStudent = isStudent, buildSerial = serial))
        }
    }

    suspend fun verifyOtp(identifier: String, code: String): LooseEnvelope {
        val serial = deviceSerial.value()
        return send(HttpMethod.Post, "/api/v1/public/auth/verify-otp", auth = false) {
            jsonBody(VerifyRequest(identifier, code, buildSerial = serial))
        }
    }

    suspend fun requestLoginOtp(identifier: String): LooseEnvelope {
        val serial = deviceSerial.value()
        return send(HttpMethod.Post, "/api/v1/public/auth/login/request-otp", auth = false) {
            jsonBody(IdentifiantRequest(identifier, buildSerial = serial))
        }
    }

    suspend fun verifyLoginOtp(identifier: String, code: String): LooseEnvelope {
        val serial = deviceSerial.value()
        return send(HttpMethod.Post, "/api/v1/public/auth/login/verify-otp", auth = false) {
            jsonBody(VerifyRequest(identifier, code, buildSerial = serial))
        }
    }

    suspend fun resendOtp(identifier: String): LooseEnvelope {
        val serial = deviceSerial.value()
        return send(HttpMethod.Post, "/api/v1/public/auth/resend-otp", auth = false) {
            jsonBody(IdentifiantRequest(identifier, buildSerial = serial))
        }
    }

    suspend fun completeProfile(fullName: String, email: String): UserDto =
        send<ApiEnvelope<UserDto>>(HttpMethod.Post, "/api/v1/auth/complete-profile") {
            jsonBody(ProfileCompletionRequest(fullName, email))
        }.required()

    suspend fun getProfile(): UserDto =
        send<ApiEnvelope<UserDto>>(HttpMethod.Get, "/api/v1/auth/profile").required()

    suspend fun updateProfile(
        fullName: String? = null,
        email: String? = null,
        city: String? = null,
        country: String? = null,
    ): UserDto =
        send<ApiEnvelope<UserDto>>(HttpMethod.Put, "/api/v1/auth/profile") {
            jsonBody(UpdateProfileRequest(fullName = fullName, email = email, city = city, country = country))
        }.required()

    suspend fun savePreferences(domainIds: List<Long>): UserPreferencesDto =
        send<ApiEnvelope<UserPreferencesDto>>(HttpMethod.Put, "/api/v1/auth/preferences") {
            jsonBody(SaveUserPreferencesRequest(domainIds))
        }.required()

    suspend fun getPreferences(): UserPreferencesDto =
        send<ApiEnvelope<UserPreferencesDto>>(HttpMethod.Get, "/api/v1/auth/preferences").required()

    suspend fun listDomains(): List<SearchDomainDto> =
        send<ApiEnvelope<List<SearchDomainDto>>>(
            HttpMethod.Get,
            "/api/v1/public/catalog/domains",
            auth = false,
        ).data.orEmpty()

    suspend fun listCities(): List<CongoCityDto> =
        send<ApiEnvelope<List<CongoCityDto>>>(
            HttpMethod.Get,
            "/api/v1/public/catalog/cities",
            auth = false,
        ).data.orEmpty()

    suspend fun listOffers(
        city: String? = null,
        province: String? = null,
        domainIds: List<Long> = emptyList(),
        page: Int = 0,
        size: Int = 50,
        status: String? = "ECHEANCE_NON_DEPASSEE",
    ): JobOfferPageDto {
        val envelope: ApiEnvelope<JobOfferPageDto> = send(HttpMethod.Get, "/api/offres", auth = false) {
            city?.takeIf { it.isNotBlank() }?.let { parameter("city", it) }
            province?.takeIf { it.isNotBlank() }?.let { parameter("province", it) }
            domainIds.forEach { parameter("domainId", it) }
            parameter("page", page)
            parameter("size", size)
            status?.let { parameter("status", it) }
        }
        return envelope.data ?: JobOfferPageDto()
    }

    suspend fun listAbonnements(): List<AbonnementDto> =
        send<ApiEnvelope<List<AbonnementDto>>>(
            HttpMethod.Get,
            "/api/v1/public/abonnements",
            auth = false,
        ).data.orEmpty()

    suspend fun listDevises(): List<DeviseDto> =
        send<ApiEnvelope<List<DeviseDto>>>(
            HttpMethod.Get,
            "/api/v1/public/devises",
            auth = false,
        ).data.orEmpty()

    suspend fun payMobileMoney(abonnementId: Long, devise: String, phone: String): FlexPaymentResponse =
        send<ApiEnvelope<FlexPaymentResponse>>(HttpMethod.Post, "/api/v1/auth/payment/mobile-money") {
            jsonBody(PaymentInitRequest(abonnementId = abonnementId, devise = devise, phone = phone))
        }.required()

    suspend fun payWithCard(abonnementId: Long, devise: String, phone: String): FlexPaymentResponse =
        send<ApiEnvelope<FlexPaymentResponse>>(HttpMethod.Post, "/api/v1/auth/payment/card") {
            jsonBody(PaymentInitRequest(abonnementId = abonnementId, devise = devise, phone = phone))
        }.required()

    suspend fun paymentHistory(): List<PaiementDto> =
        send<ApiEnvelope<List<PaiementDto>>>(HttpMethod.Get, "/api/v1/auth/payment/history").data.orEmpty()

    private inline fun <reified T> HttpRequestBuilder.jsonBody(body: T) {
        setBody(TextContent(json.encodeToString(body), ContentType.Application.Json))
    }

    private fun <T> ApiEnvelope<T>.required(): T =
        data ?: throw ApiException(message.ifBlank { "Réponse inattendue du serveur." })

    private suspend inline fun <reified T> send(
        method: HttpMethod,
        path: String,
        auth: Boolean = true,
        builder: HttpRequestBuilder.() -> Unit = {},
    ): T {
        val response = try {
            http.request {
                this.method = method
                url { takeFrom("${config.apiBaseUrl}$path") }
                applyDeviceSerialHeaders(deviceSerial.value())
                if (auth) {
                    tokenStore.accessToken?.takeIf { it.isNotBlank() }?.let {
                        header(HttpHeaders.Authorization, "Bearer $it")
                    }
                }
                builder()
            }
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            if (error is ApiException) throw error
            throw ApiException(mapClientTransportError(error), status = 0)
        }
        val text = response.bodyAsText()
        if (!response.status.isSuccess()) {
            throw ApiException.fromBody(response.status.value, text)
        }
        return try {
            json.decodeFromString(text)
        } catch (_: Exception) {
            throw ApiException("Réponse inattendue du serveur.", response.status.value)
        }
    }
}

internal fun mapClientTransportError(error: Throwable): String {
    val parts = mutableListOf<String>()
    var current: Throwable? = error
    while (current != null) {
        parts += current::class.simpleName.orEmpty()
        parts += current.message.orEmpty()
        current = current.cause
    }
    val blob = parts.joinToString(" ")
    val network = listOf(
        "UnknownHost",
        "Unable to resolve",
        "Failed to connect",
        "ConnectException",
        "SocketTimeout",
        "timed out",
        "Timeout",
        "Unreachable",
        "Network is unreachable",
        "Connection reset",
    ).any { blob.contains(it, ignoreCase = true) }
    return if (network) "Vérifie ta connexion." else "Impossible d’envoyer la requête. Réessaie."
}
