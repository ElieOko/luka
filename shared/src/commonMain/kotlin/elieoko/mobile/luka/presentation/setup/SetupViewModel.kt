package elieoko.mobile.luka.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.City
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.TradeChip
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.usecase.CompleteLocationUseCase
import elieoko.mobile.luka.domain.usecase.CompleteProfessionUseCase
import elieoko.mobile.luka.domain.usecase.LaunchInfiniteAnalysisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SetupUiState(
    val trades: List<TradeChip> = TradeChip.fromLocal(),
    val cities: List<City> = CongoCatalog.cities,
    val selectedProfession: Profession? = null,
    val selectedDomainId: Long? = null,
    val selectedRegionId: String? = null,
    val selectedCityName: String? = null,
    val launching: Boolean = false,
    val launched: Boolean = false,
    val error: String? = null,
)

class SetupViewModel(
    private val completeProfession: CompleteProfessionUseCase,
    private val completeLocation: CompleteLocationUseCase,
    private val launchAnalysis: LaunchInfiniteAnalysisUseCase,
    catalog: CatalogRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SetupUiState())
    val state: StateFlow<SetupUiState> = _state

    init {
        viewModelScope.launch {
            runCatching { catalog.loadPublicCatalog() }
                .onSuccess { public ->
                    _state.update {
                        it.copy(
                            trades = public.trades.ifEmpty { it.trades },
                            cities = public.cities.ifEmpty { it.cities },
                        )
                    }
                }
                .onFailure { error -> _state.update { it.copy(error = error.message) } }
        }
    }

    fun selectProfession(chip: TradeChip) {
        _state.update {
            it.copy(
                selectedProfession = chip.profession,
                selectedDomainId = chip.domainId,
                error = null,
            )
        }
    }

    fun confirmProfession() {
        val profession = _state.value.selectedProfession ?: return
        viewModelScope.launch {
            runCatching { completeProfession(profession, _state.value.selectedDomainId) }
                .onFailure { error -> _state.update { it.copy(error = error.message) } }
        }
    }

    fun selectCity(city: City) = _state.update {
        it.copy(selectedRegionId = city.regionId, selectedCityName = city.name, error = null)
    }

    fun confirmLocation() {
        val regionId = _state.value.selectedRegionId ?: return
        viewModelScope.launch {
            runCatching { completeLocation(regionId, _state.value.selectedCityName) }
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
