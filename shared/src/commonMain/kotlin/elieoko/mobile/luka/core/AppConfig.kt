package elieoko.mobile.luka.core

/**
 * Runtime configuration for third-party services.
 * Leave secrets empty in source control; fill them from your environment before shipping.
 */
data class AppConfig(
    val sentryDsn: String = "",
    val oneSignalAppId: String = "",
    val apiBaseUrl: String = "https://server.casanayo.com",
    val stompUrl: String = "wss://api.luka.cd/ws",
    val stompDestination: String = "/topic/offers.rdc",
    val stompEnabled: Boolean = false,
    val demoOtpCode: String = "123456",
)
