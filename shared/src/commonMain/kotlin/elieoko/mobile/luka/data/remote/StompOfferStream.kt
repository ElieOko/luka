package elieoko.mobile.luka.data.remote

import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.CrashReporter
import elieoko.mobile.luka.domain.model.JobOffer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient

interface OfferStream {
    fun observe(): Flow<JobOffer>
}

class StompOfferStream(
    private val httpClient: HttpClient,
    private val config: AppConfig,
    private val crashReporter: CrashReporter,
    private val json: Json,
) : OfferStream {
    override fun observe(): Flow<JobOffer> {
        if (!config.stompEnabled) return emptyFlow()
        return flow {
            try {
                val stomp = StompClient(KtorWebSocketClient(httpClient.config { install(WebSockets) }))
                val session = stomp.connect(config.stompUrl)
                session.subscribeText(config.stompDestination).collect { body ->
                    emit(json.decodeFromString<OfferDto>(body).toDomain())
                }
            } catch (error: Exception) {
                crashReporter.capture(error)
            }
        }
    }
}

@Serializable
data class OfferDto(
    val id: String,
    val title: String,
    val company: String,
    val companyLogoUrl: String,
    val professionId: String,
    val regionId: String,
    val city: String,
    val contract: String,
    val salary: String,
    val summary: String,
    val applyUrl: String,
    val postedAtEpochMs: Long,
    val isRemote: Boolean,
)

private fun OfferDto.toDomain() = JobOffer(
    id = id,
    title = title,
    company = company,
    companyLogoUrl = companyLogoUrl,
    profession = elieoko.mobile.luka.domain.model.Profession.fromId(professionId),
    regionId = regionId,
    city = city,
    contract = contract,
    salary = salary,
    summary = summary,
    applyUrl = applyUrl,
    postedAtEpochMs = postedAtEpochMs,
    isRemote = isRemote,
)
