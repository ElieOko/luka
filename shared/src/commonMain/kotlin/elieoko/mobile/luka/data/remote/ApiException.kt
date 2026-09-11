package elieoko.mobile.luka.data.remote

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiException(
    message: String,
    val status: Int = 0,
) : Exception(message) {
    companion object {
        private val json = Json { ignoreUnknownKeys = true; isLenient = true }

        fun fromBody(status: Int, body: String): ApiException {
            val parsed = runCatching {
                val obj = json.parseToJsonElement(body).jsonObject
                obj["message"]?.jsonPrimitive?.content
                    ?: obj["detailMessage"]?.jsonPrimitive?.content
                    ?: obj["error"]?.jsonPrimitive?.content
            }.getOrNull()?.takeIf { it.isNotBlank() }
            return ApiException(parsed ?: fallback(status), status)
        }

        private fun fallback(status: Int) = when (status) {
            401 -> "Session expirée. Reconnecte-toi."
            403 -> "Accès refusé."
            in 500..599 -> "Le serveur Luka est indisponible. Réessaie dans un instant."
            else -> "Impossible de joindre Luka ($status)."
        }
    }
}
