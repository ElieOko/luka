package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.AppDestination
import elieoko.mobile.luka.domain.model.UserSession

class ResolveDestinationUseCase {
    operator fun invoke(session: UserSession?): AppDestination {
        if (session == null || !session.isAuthenticated) return AppDestination.Welcome
        val profile = session.profile
        return when {
            profile.profession == null -> AppDestination.Profession
            profile.countryCode.isNullOrBlank() || profile.regionId.isNullOrBlank() -> AppDestination.Location
            !profile.analysisLaunched -> AppDestination.Analysis
            else -> AppDestination.Home
        }
    }
}
