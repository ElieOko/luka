package elieoko.mobile.luka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class OfferEntity(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val companyLogoUrl: String,
    val professionId: String,
    val regionId: String,
    val city: String,
    val contract: String,
    val salary: String,
    val summary: String,
    val applyUrl: String,
    val postedAtEpochMs: Long,
    val isRemote: Boolean,
)

@Entity(tableName = "ads")
data class AdEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val cta: String,
    val imageName: String,
    val destination: String,
)

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val excerpt: String,
    val source: String,
    val imageName: String,
    val publishedAtEpochMs: Long,
    val url: String,
)

@Entity(tableName = "professionals")
data class ProfessionalEntity(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val professionId: String,
    val city: String,
    val bio: String,
    val photoName: String,
    val rating: Double,
    val missions: Int,
    val available: Boolean,
)

@Entity(tableName = "orientation")
data class OrientationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val domain: String,
    val duration: String,
    val level: String,
    val why: String,
    val nextStep: String,
    val imageName: String,
    val demandScore: Int,
)

@Entity(tableName = "demand_stats")
data class DemandStatEntity(
    @PrimaryKey val professionId: String,
    val openings: Int,
    val sharePercent: Int,
    val trend: String,
)

@Entity(tableName = "meta")
data class MetaEntity(
    @PrimaryKey val key: String,
    val value: String,
)
