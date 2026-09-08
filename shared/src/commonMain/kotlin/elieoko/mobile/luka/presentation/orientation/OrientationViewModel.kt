package elieoko.mobile.luka.presentation.orientation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.data.remote.FakeCatalog
import elieoko.mobile.luka.domain.model.NationalInsight
import elieoko.mobile.luka.domain.model.OrientationPersona
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class OrientationPhase { Pick, Analyzing, Results }

data class OrientationUiState(
    val persona: OrientationPersona? = null,
    val phase: OrientationPhase = OrientationPhase.Pick,
    val insights: List<NationalInsight> = emptyList(),
)

class OrientationViewModel : ViewModel() {
    private val _state = MutableStateFlow(OrientationUiState())
    val state: StateFlow<OrientationUiState> = _state

    fun select(persona: OrientationPersona) {
        _state.update { it.copy(persona = persona) }
    }

    fun analyze() {
        val persona = _state.value.persona ?: return
        viewModelScope.launch {
            _state.update { it.copy(phase = OrientationPhase.Analyzing) }
            delay(2200)
            _state.update {
                it.copy(
                    phase = OrientationPhase.Results,
                    insights = FakeCatalog.insightsFor(persona),
                )
            }
        }
    }

    fun reset() {
        _state.value = OrientationUiState()
    }
}
