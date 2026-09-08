package elieoko.mobile.luka.domain.model

enum class AuthChannel { PHONE, EMAIL }

data class AuthIdentifier(
    val channel: AuthChannel,
    val value: String,
)

data class OtpChallenge(
    val identifier: AuthIdentifier,
    val expiresInSeconds: Int = 300,
)
