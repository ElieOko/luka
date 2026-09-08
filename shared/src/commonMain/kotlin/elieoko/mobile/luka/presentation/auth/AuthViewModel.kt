package elieoko.mobile.luka.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.usecase.RequestOtpUseCase
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
) {
    enum class Step { Identifier, Otp }
}

class AuthViewModel(
    private val requestOtp: RequestOtpUseCase,
    private val verifyOtp: VerifyOtpUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun onInput(value: String) = _state.update { it.copy(input = value, error = null) }
    fun onOtp(value: String) = _state.update { it.copy(otp = value.filter { ch -> ch.isDigit() }.take(6), error = null) }

    fun submitIdentifier() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { requestOtp(_state.value.input) }
                .onSuccess { challenge ->
                    _state.update { it.copy(loading = false, step = AuthUiState.Step.Otp, identifier = challenge.identifier) }
                }
                .onFailure { error ->
                    _state.update { it.copy(loading = false, error = error.message) }
                }
        }
    }

    fun submitOtp() {
        val identifier = _state.value.identifier ?: return
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { verifyOtp(identifier, _state.value.otp) }
                .onFailure { error ->
                    _state.update { it.copy(loading = false, error = error.message) }
                }
        }
    }
}
