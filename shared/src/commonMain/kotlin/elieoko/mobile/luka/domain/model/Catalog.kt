package elieoko.mobile.luka.domain.model

data class JobOffer(
    val id: String,
    val title: String,
    val company: String,
    val companyLogoUrl: String,
    val profession: Profession,
    val regionId: String,
    val city: String,
    val contract: String,
    val salary: String,
    val summary: String,
    val applyUrl: String,
    val postedAtEpochMs: Long,
    val isRemote: Boolean,
)

data class PlatformAd(
    val id: String,
    val title: String,
    val subtitle: String,
    val cta: String,
    val imageName: String,
    val destination: String,
)

data class NewsItem(
    val id: String,
    val title: String,
    val excerpt: String,
    val source: String,
    val imageName: String,
    val publishedAtEpochMs: Long,
    val url: String,
)

data class Professional(
    val id: String,
    val name: String,
    val title: String,
    val profession: Profession,
    val city: String,
    val bio: String,
    val photoName: String,
    val rating: Double,
    val missions: Int,
    val available: Boolean,
)

data class OrientationPath(
    val id: String,
    val title: String,
    val domain: String,
    val duration: String,
    val level: String,
    val why: String,
    val nextStep: String,
    val imageName: String,
    val demandScore: Int,
)

data class DemandStat(
    val profession: Profession,
    val openings: Int,
    val sharePercent: Int,
    val trend: String,
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceLabel: String,
    val period: String,
    val highlight: Boolean,
    val professionSlots: Int,
    val recruiterVisible: Boolean,
    val orientationPlus: Boolean,
    val perks: List<String>,
)

data class HomeFeed(
    val offers: List<JobOffer>,
    val ads: List<PlatformAd>,
    val news: List<NewsItem>,
    val stats: List<DemandStat>,
    val orientation: List<OrientationPath>,
    val professionals: List<Professional>,
    val topProfession: DemandStat?,
)
