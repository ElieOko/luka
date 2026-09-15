package elieoko.mobile.luka.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.AccountKind
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.AuthStartResult
import elieoko.mobile.luka.domain.usecase.RequestOtpUseCase
import elieoko.mobile.luka.domain.usecase.ResendOtpUseCase
import elieoko.mobile.luka.domain.usecase.VerifyOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val input: String = "",
    val otp: String = "",
    val step: Step = Step.Identifier,
    val loading: Boolean = false,
    val error: String? = null,
    val identifier: AuthIdentifier? = null,
    val info: String? = null,
    val newAccount: Boolean = false,
    val accountKind: AccountKind? = null,
    val privacyAccepted: Boolean = false,
    val showPrivacy: Boolean = false,
) {
    enum class Step { Identifier, Otp }
}

class AuthViewModel(
    private val requestOtp: RequestOtpUseCase,
    private val verifyOtp: VerifyOtpUseCase,
    private val resendOtp: ResendOtpUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun onInput(value: String) = _state.update { it.copy(input = value, error = null, info = null) }
    fun onOtp(value: String) = _state.update { it.copy(otp = value.filter { ch -> ch.isDigit() }.take(6), error = null) }

    fun toggleNewAccount() = _state.update {
        it.copy(newAccount = !it.newAccount, error = null, info = null, step = AuthUiState.Step.Identifier, otp = "", accountKind = null, privacyAccepted = false)
    }

    fun selectAccountKind(kind: AccountKind) = _state.update { it.copy(accountKind = kind, error = null) }
    fun togglePrivacy() = _state.update { it.copy(privacyAccepted = !it.privacyAccepted, error = null) }
    fun openPrivacy() = _state.update { it.copy(showPrivacy = true) }
    fun closePrivacy() = _state.update { it.copy(showPrivacy = false) }

    fun submitIdentifier() {
        val current = _state.value
        if (current.newAccount && current.accountKind == null) {
            _state.update { it.copy(error = "Choisis un compte apprenant ou professionnel.") }
            return
        }
        if (current.newAccount && !current.privacyAccepted) {
            _state.update { it.copy(error = "Accepte la politique de confidentialité pour continuer.") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, info = null) }
            runCatching { requestOtp(current.input, current.newAccount, current.accountKind) }
                .onSuccess { result ->
                    when (result) {
                        is AuthStartResult.SignedIn -> _state.update {
                            it.copy(loading = false, error = null, info = null)
                        }
                        is AuthStartResult.OtpRequired -> _state.update {
                            it.copy(
                                loading = false,
                                step = AuthUiState.Step.Otp,
                                identifier = result.challenge.identifier,
                                info = "Code envoyé par SMS.",
                            )
                        }
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(loading = false, error = error.message) }
                }
        }
    }

    fun submitOtp() {
        val identifier = _state.value.identifier ?: return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, info = null) }
            runCatching { verifyOtp(identifier, _state.value.otp) }
                .onFailure { error ->
                    _state.update { it.copy(loading = false, error = error.message) }
                }
        }
    }

    fun resend() {
        val identifier = _state.value.identifier ?: return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { resendOtp(identifier) }
                .onSuccess {
                    _state.update { it.copy(loading = false, info = "Nouveau code envoyé.", otp = "") }
                }
                .onFailure { error ->
                    _state.update { it.copy(loading = false, error = error.message) }
                }
        }
    }
}
