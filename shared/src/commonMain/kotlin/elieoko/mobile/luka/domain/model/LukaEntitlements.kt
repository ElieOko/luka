package elieoko.mobile.luka.domain.model

object LukaEntitlements {
    fun learnerUnlocked(profile: UserProfile): Boolean =
        profile.isPremium || LukaPlans.isStudent(profile.planId) || LukaPlans.isProfessional(profile.planId)

    fun proUnlocked(profile: UserProfile): Boolean =
        profile.isPremium || LukaPlans.isProfessional(profile.planId)

    fun fullMitNews(profile: UserProfile) = learnerUnlocked(profile)
    fun fullTrends(profile: UserProfile) = learnerUnlocked(profile) || proUnlocked(profile)
    fun fullOrientation(profile: UserProfile) = learnerUnlocked(profile)
    fun regularAdvice(profile: UserProfile) = learnerUnlocked(profile)
    fun allOffers(profile: UserProfile) = proUnlocked(profile)
    fun cvAnalysis(profile: UserProfile) = proUnlocked(profile)
    fun offerAnalysis(profile: UserProfile) = proUnlocked(profile)
    fun realtimeMarket(profile: UserProfile) = proUnlocked(profile)

    fun offerPreviewLimit(profile: UserProfile): Int = if (allOffers(profile)) Int.MAX_VALUE else 5
    fun advicePreviewLimit(profile: UserProfile): Int = if (regularAdvice(profile)) Int.MAX_VALUE else 1
    fun newsPreviewLimit(profile: UserProfile): Int = if (fullMitNews(profile)) Int.MAX_VALUE else 2

    fun suggestedPlan(kind: AccountKind): SubscriptionPlan = when (kind) {
        AccountKind.LEARNER -> LukaPlans.student
        AccountKind.PROFESSIONAL -> LukaPlans.professional
    }
}
