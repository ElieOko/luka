package elieoko.mobile.luka.data.remote

import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.data.remote.dto.ApiEnvelope
import elieoko.mobile.luka.data.remote.dto.CongoCityDto
import elieoko.mobile.luka.data.remote.dto.IdentifiantRequest
import elieoko.mobile.luka.data.remote.dto.JobOfferPageDto
import elieoko.mobile.luka.data.remote.dto.LooseEnvelope
import elieoko.mobile.luka.data.remote.dto.PhoneRegisterRequest
import elieoko.mobile.luka.data.remote.dto.ProfileCompletionRequest
import elieoko.mobile.luka.data.remote.dto.SaveUserPreferencesRequest
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
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class LukaApi(
    private val http: HttpClient,
    private val config: AppConfig,
    private val tokenStore: TokenStore,
    private val json: Json,
) {
    suspend fun registerPhone(phone: String): LooseEnvelope =
        send(HttpMethod.Post, "/api/v1/public/auth/register-phone", auth = false) {
            jsonBody(PhoneRegisterRequest(phone))
        }

    suspend fun verifyOtp(identifier: String, code: String): LooseEnvelope =
        send(HttpMethod.Post, "/api/v1/public/auth/verify-otp", auth = false) {
            jsonBody(VerifyRequest(identifier, code))
        }

    suspend fun resendOtp(identifier: String): LooseEnvelope =
        send(HttpMethod.Post, "/api/v1/public/auth/resend-otp", auth = false) {
            jsonBody(IdentifiantRequest(identifier))
        }

    suspend fun completeProfile(fullName: String, email: String): UserDto =
        send<ApiEnvelope<UserDto>>(HttpMethod.Post, "/api/v1/auth/complete-profile") {
            jsonBody(ProfileCompletionRequest(fullName, email))
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

    private fun HttpRequestBuilder.jsonBody(body: Any) {
        contentType(ContentType.Application.Json)
        setBody(body)
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
                if (auth) {
                    tokenStore.accessToken?.takeIf { it.isNotBlank() }?.let {
                        header(HttpHeaders.Authorization, "Bearer $it")
                    }
                }
                builder()
            }
        } catch (error: Exception) {
            if (error is ApiException) throw error
            throw ApiException("Vérifie ta connexion.", status = 0)
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
