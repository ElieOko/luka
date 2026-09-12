package elieoko.mobile.luka.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Protocol

actual fun createHttpClient(): HttpClient = HttpClient(OkHttp) {
    engine {
        config {
            protocols(listOf(Protocol.HTTP_1_1))
        }
    }
    install(WebSockets)
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true; encodeDefaults = true })
    }
}
