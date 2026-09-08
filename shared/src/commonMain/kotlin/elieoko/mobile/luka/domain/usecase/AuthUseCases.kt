package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.AuthChannel
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.OtpChallenge
import elieoko.mobile.luka.domain.model.UserSession
import elieoko.mobile.luka.domain.repository.SessionRepository

class RequestOtpUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(raw: String): OtpChallenge {
        val identifier = parseIdentifier(raw)
        return sessions.requestOtp(identifier)
    }

    companion object {
        fun parseIdentifier(raw: String): AuthIdentifier {
            val value = raw.trim()
            require(value.isNotBlank()) { "Indique un numéro ou un e-mail." }
            val channel = if (value.contains("@")) AuthChannel.EMAIL else AuthChannel.PHONE
            if (channel == AuthChannel.EMAIL) {
                require(value.contains(".") && value.length >= 6) { "E-mail invalide." }
            } else {
                val digits = value.filter { it.isDigit() }
                require(digits.length >= 9) { "Numéro trop court. Exemple : +243 81 000 0000" }
            }
            return AuthIdentifier(channel, value)
        }
    }
}

class VerifyOtpUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(identifier: AuthIdentifier, code: String): UserSession {
        require(code.length == 6) { "Le code a 6 chiffres." }
        return sessions.verifyOtp(identifier, code)
    }
}
