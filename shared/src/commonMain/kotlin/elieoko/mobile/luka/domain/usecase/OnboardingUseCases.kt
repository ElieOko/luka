package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository

class CompleteProfessionUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(profession: Profession, domainId: Long? = null) {
        sessions.saveProfession(profession.id, domainId)
    }
}

class CompleteLocationUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(regionId: String, cityName: String? = null) {
        sessions.saveLocation(countryCode = "CD", regionId = regionId, cityName = cityName)
    }
}

class LaunchInfiniteAnalysisUseCase(
    private val sessions: SessionRepository,
    private val catalog: CatalogRepository,
) {
    suspend operator fun invoke() {
        val current = sessions.current() ?: error("Session expirée")
        check(!current.profile.analysisLaunched) { "Les analyses infinies sont déjà en cours." }
        catalog.seedIfNeeded()
        catalog.refreshOffers(current.profile)
        sessions.markAnalysisLaunched()
    }
}

class UpdateProfileUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(
        displayName: String,
        bio: String,
        email: String = "",
        cityName: String? = null,
    ) {
        require(displayName.trim().length >= 2) { "Le nom est trop court." }
        if (email.isNotBlank()) {
            require(email.contains("@") && email.contains(".")) { "E-mail invalide." }
        }
        sessions.updateProfile(displayName.trim(), bio.trim(), email.trim(), cityName?.trim()?.ifBlank { null })
    }
}

class SelectPlanUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(planId: String, extraProfessionIds: List<String>) {
        sessions.selectPlan(planId, extraProfessionIds.distinct())
    }
}
