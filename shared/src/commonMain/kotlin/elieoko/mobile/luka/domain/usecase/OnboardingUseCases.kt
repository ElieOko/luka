package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.repository.SessionRepository

class CompleteProfessionUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(profession: Profession) {
        sessions.saveProfession(profession.id)
    }
}

class CompleteLocationUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(regionId: String) {
        sessions.saveLocation(countryCode = "CD", regionId = regionId)
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
        sessions.markAnalysisLaunched()
    }
}

class UpdateProfileUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(displayName: String, bio: String) {
        require(displayName.trim().length >= 2) { "Le nom est trop court." }
        sessions.updateProfile(displayName.trim(), bio.trim())
    }
}

class SelectPlanUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke(planId: String, extraProfessionIds: List<String>) {
        sessions.selectPlan(planId, extraProfessionIds.distinct())
    }
}
