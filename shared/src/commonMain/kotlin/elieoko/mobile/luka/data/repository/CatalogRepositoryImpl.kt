package elieoko.mobile.luka.data.repository

import elieoko.mobile.luka.core.CrashReporter
import elieoko.mobile.luka.data.local.LukaDatabase
import elieoko.mobile.luka.data.local.entity.MetaEntity
import elieoko.mobile.luka.data.mapper.toDomain
import elieoko.mobile.luka.data.mapper.toEntity
import elieoko.mobile.luka.data.mapper.toJobOffer
import elieoko.mobile.luka.data.mapper.toCity
import elieoko.mobile.luka.data.mapper.toTradeChips
import elieoko.mobile.luka.data.remote.LukaApi
import elieoko.mobile.luka.data.remote.OfferStream
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.HomeFeed
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.PublicCatalog
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.model.TradeChip
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.repository.CatalogRepository
import elieoko.mobile.luka.domain.usecase.LiveInsights
import elieoko.mobile.luka.domain.usecase.withPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach

class CatalogRepositoryImpl(
    private val database: LukaDatabase,
    private val offerStream: OfferStream,
    private val api: LukaApi,
    private val crashReporter: CrashReporter,
) : CatalogRepository {

    override fun observeFeed(profile: UserProfile): Flow<HomeFeed> {
        val plan = LukaPlans.byId(profile.planId)
        return combine(
            database.offerDao().observeAll(),
            database.adDao().observeAll(),
            database.newsDao().observeAll(),
        ) { offerRows, ads, news ->
            val offers = offerRows.map { it.toDomain() }
            val demand = LiveInsights.demandFrom(offers)
            HomeFeed(
                offers = offers,
                ads = ads.map { it.toDomain() },
                news = news.map { it.toDomain() },
                stats = demand,
                orientation = emptyList(),
                professionals = emptyList(),
                topProfession = demand.maxByOrNull { it.openings },
            ).withPolicy(profile, plan)
        }
    }

    override fun observeLiveOffers(): Flow<JobOffer> =
        offerStream.observe().onEach { database.offerDao().upsert(it.toEntity()) }

    override suspend fun seedIfNeeded() {
        database.adDao().clear()
        database.newsDao().clear()
        database.professionalDao().clear()
        database.orientationDao().clear()
        database.demandDao().clear()
        if (database.metaDao().get("seeded")?.value != "v6") {
            database.offerDao().clear()
            database.metaDao().put(MetaEntity("seeded", "v6"))
        }
        loadPublicCatalog()
    }

    override suspend fun loadPublicCatalog(): PublicCatalog {
        val cities = runCatching { api.listCities().filter { it.isActive }.map { it.toCity() } }
            .onFailure { crashReporter.capture(it) }
            .getOrDefault(emptyList())
        val trades = runCatching { api.listDomains().toTradeChips() }
            .onFailure { crashReporter.capture(it) }
            .getOrDefault(emptyList())
        val catalog = PublicCatalog(
            cities = cities.ifEmpty { CongoCatalog.cities },
            trades = trades.ifEmpty { TradeChip.fromLocal() },
        )
        return catalog
    }

    override suspend fun refreshOffers(profile: UserProfile?) {
        val page = runCatching {
            api.listOffers(
                city = profile?.cityName,
                domainIds = listOfNotNull(profile?.domainId),
                size = 50,
            )
        }.onFailure { crashReporter.capture(it) }.getOrNull() ?: return
        database.offerDao().clear()
        val offers = page.content.map { it.toJobOffer() }
        if (offers.isNotEmpty()) {
            database.offerDao().upsert(offers.map { it.toEntity() })
        }
    }

    override fun plans(): List<SubscriptionPlan> = LukaPlans.all
}
