package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.AuthChannel
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.OtpChallenge
import elieoko.mobile.luka.domain.model.UserSession
import elieoko.mobile.luka.domain.repository.SessionRepository

class RequestOtpUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(raw: String, newAccount: Boolean = false): OtpChallenge {
        val identifier = parseIdentifier(raw)
        return sessions.requestOtp(identifier, newAccount)
    }

    companion object {
        fun parseIdentifier(raw: String): AuthIdentifier {
            val value = raw.trim()
            require(value.isNotBlank()) { "Indique un numéro congolais." }
            require(!value.contains("@")) { "Indique un numéro congolais. L’e-mail arrive plus tard." }
            val digits = value.filter { it.isDigit() }
            require(digits.length >= 9) { "Numéro trop court. Exemple : +243 81 000 0000" }
            return AuthIdentifier(AuthChannel.PHONE, value)
        }
    }
}

class ResendOtpUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(identifier: AuthIdentifier) = sessions.resendOtp(identifier)
}

class VerifyOtpUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(identifier: AuthIdentifier, code: String): UserSession {
        require(code.length == 6) { "Le code a 6 chiffres." }
        return sessions.verifyOtp(identifier, code)
    }
}
