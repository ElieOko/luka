package elieoko.mobile.luka.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.domain.model.City
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.domain.usecase.SelectPlanUseCase
import elieoko.mobile.luka.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: UserProfile? = null,
    val displayName: String = "",
    val bio: String = "",
    val email: String = "",
    val cityName: String = "",
    val cities: List<City> = CongoCatalog.cities,
    val plans: List<SubscriptionPlan> = emptyList(),
    val extraProfession: Profession? = null,
    val message: String? = null,
    val saving: Boolean = false,
    val professionalQuery: String = "",
)

class ProfileViewModel(
    private val sessions: SessionRepository,
    catalog: CatalogRepository,
    private val updateProfile: UpdateProfileUseCase,
    private val selectPlan: SelectPlanUseCase,
) : ViewModel() {
    private val draft = MutableStateFlow(ProfileUiState(plans = catalog.plans()))

    val state: StateFlow<ProfileUiState> = combine(
        sessions.session.filterNotNull(),
        draft,
    ) { session, local ->
        val profile = session.profile
        local.copy(
            profile = profile,
            displayName = local.displayName.ifBlank { profile.displayName },
            bio = local.bio.ifBlank { profile.bio },
            email = local.email.ifBlank { profile.email },
            cityName = local.cityName.ifBlank { profile.cityName.orEmpty() },
            extraProfession = local.extraProfession
                ?: profile.extraProfessionIds.firstOrNull()?.let(Profession::fromId),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState(plans = catalog.plans()))

    init {
        viewModelScope.launch {
            runCatching { sessions.refreshRemoteProfile() }
            runCatching { catalog.loadPublicCatalog() }
                .onSuccess { public ->
                    draft.update { it.copy(cities = public.cities.ifEmpty { it.cities }) }
                }
        }
    }

    fun onName(value: String) = draft.update { it.copy(displayName = value, message = null) }
    fun onBio(value: String) = draft.update { it.copy(bio = value, message = null) }
    fun onEmail(value: String) = draft.update { it.copy(email = value, message = null) }
    fun onCity(value: String) = draft.update { it.copy(cityName = value, message = null) }
    fun onProfessionalQuery(value: String) = draft.update { it.copy(professionalQuery = value) }
    fun onExtraProfession(profession: Profession) = draft.update { it.copy(extraProfession = profession) }

    fun saveProfile(onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            draft.update { it.copy(saving = true, message = null) }
            runCatching {
                updateProfile(
                    displayName = draft.value.displayName.ifBlank { state.value.displayName },
                    bio = draft.value.bio.ifBlank { state.value.bio },
                    email = draft.value.email.ifBlank { state.value.email },
                    cityName = draft.value.cityName.ifBlank { state.value.cityName },
                )
            }
                .onSuccess {
                    draft.update { it.copy(saving = false, message = "Profil mis à jour") }
                    onDone?.invoke()
                }
                .onFailure { error -> draft.update { it.copy(saving = false, message = error.message) } }
        }
    }

    fun choosePlan(plan: SubscriptionPlan) {
        viewModelScope.launch {
            val extras = buildList {
                draft.value.extraProfession?.let { add(it.id) }
            }
            runCatching { selectPlan(plan.id, extras) }
                .onSuccess { draft.update { it.copy(message = "${plan.name} activé") } }
                .onFailure { error -> draft.update { it.copy(message = error.message) } }
        }
    }

    fun saveCv(document: elieoko.mobile.luka.core.CvDocument) {
        viewModelScope.launch {
            runCatching { sessions.saveCv(document.fileName, document.mimeType) }
                .onSuccess { draft.update { it.copy(message = "CV ${document.fileName} enregistré") } }
                .onFailure { error -> draft.update { it.copy(message = error.message) } }
        }
    }

    fun logout() {
        viewModelScope.launch { sessions.resetDemo() }
    }
}
