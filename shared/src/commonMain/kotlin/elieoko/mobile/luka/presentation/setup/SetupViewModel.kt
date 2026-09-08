package elieoko.mobile.luka.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.usecase.CompleteLocationUseCase
import elieoko.mobile.luka.domain.usecase.CompleteProfessionUseCase
import elieoko.mobile.luka.domain.usecase.LaunchInfiniteAnalysisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SetupUiState(
    val selectedProfession: Profession? = null,
    val selectedRegionId: String? = null,
    val launching: Boolean = false,
    val launched: Boolean = false,
    val error: String? = null,
)

class SetupViewModel(
    private val completeProfession: CompleteProfessionUseCase,
    private val completeLocation: CompleteLocationUseCase,
    private val launchAnalysis: LaunchInfiniteAnalysisUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(SetupUiState())
    val state: StateFlow<SetupUiState> = _state

    fun selectProfession(profession: Profession) {
        _state.update { it.copy(selectedProfession = profession, error = null) }
    }

    fun confirmProfession() {
        val profession = _state.value.selectedProfession ?: return
        viewModelScope.launch {
            runCatching { completeProfession(profession) }
                .onFailure { error -> _state.update { it.copy(error = error.message) } }
        }
    }

    fun selectRegion(id: String) = _state.update { it.copy(selectedRegionId = id, error = null) }

    fun confirmLocation() {
        val regionId = _state.value.selectedRegionId ?: return
        viewModelScope.launch {
            runCatching { completeLocation(regionId) }
                .onFailure { error -> _state.update { it.copy(error = error.message) } }
        }
    }

    fun launch() {
        viewModelScope.launch {
            _state.update { it.copy(launching = true, error = null) }
            runCatching { launchAnalysis() }
                .onSuccess { _state.update { it.copy(launching = false, launched = true) } }
                .onFailure { error -> _state.update { it.copy(launching = false, error = error.message) } }
        }
    }
}
