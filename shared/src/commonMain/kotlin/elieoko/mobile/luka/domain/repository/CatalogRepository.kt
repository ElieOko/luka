package elieoko.mobile.luka.domain.repository

import elieoko.mobile.luka.domain.model.HomeFeed
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.PublicCatalog
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface CatalogRepository {
    fun observeFeed(profile: UserProfile): Flow<HomeFeed>
    fun observeLiveOffers(): Flow<JobOffer>
    suspend fun seedIfNeeded()
    suspend fun loadPublicCatalog(): PublicCatalog
    suspend fun refreshOffers(profile: UserProfile? = null)
    fun plans(): List<SubscriptionPlan>
}
