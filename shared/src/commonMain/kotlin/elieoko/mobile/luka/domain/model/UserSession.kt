package elieoko.mobile.luka.domain.model

data class UserProfile(
    val id: String,
    val displayName: String,
    val bio: String,
    val photoUrl: String,
    val identifier: AuthIdentifier,
    val profession: Profession?,
    val countryCode: String?,
    val regionId: String?,
    val planId: String,
    val extraProfessionIds: List<String>,
    val analysisLaunched: Boolean,
    val welcomeSeen: Boolean,
    val visibleToRecruiters: Boolean,
    val cvFileName: String = "",
    val cvMime: String = "",
)

data class UserSession(
    val token: String,
    val profile: UserProfile,
) {
    val isAuthenticated: Boolean get() = token.isNotBlank()
    val needsWelcome: Boolean get() = !profile.welcomeSeen
    val needsProfession: Boolean get() = profile.profession == null
    val needsLocation: Boolean get() = profile.countryCode.isNullOrBlank() || profile.regionId.isNullOrBlank()
    val needsAnalysis: Boolean get() = !profile.analysisLaunched
}

enum class AppDestination {
    Welcome,
    Auth,
    Profession,
    Location,
    Analysis,
    Home,
}
