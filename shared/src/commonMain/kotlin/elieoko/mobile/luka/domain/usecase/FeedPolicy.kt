package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.HomeFeed
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.model.UserProfile

object FeedPolicy {
    fun allowedProfessions(profile: UserProfile, plan: SubscriptionPlan): Set<Profession> {
        val primary = profile.profession ?: return emptySet()
        if (plan.professionSlots >= Profession.entries.size) {
            return Profession.entries.toSet()
        }
        val extras = profile.extraProfessionIds.map(Profession::fromId)
        return (listOf(primary) + extras).take(plan.professionSlots).toSet()
    }

    fun filterOffers(offers: List<JobOffer>, allowed: Set<Profession>): List<JobOffer> =
        offers.filter { it.profession in allowed }
}

fun HomeFeed.withPolicy(profile: UserProfile, plan: SubscriptionPlan): HomeFeed {
    if (profile.domainId != null) return this
    val allowed = FeedPolicy.allowedProfessions(profile, plan)
    return copy(offers = FeedPolicy.filterOffers(offers, allowed))
}
