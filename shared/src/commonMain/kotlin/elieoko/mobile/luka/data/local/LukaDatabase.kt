package elieoko.mobile.luka.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import elieoko.mobile.luka.data.local.dao.AdDao
import elieoko.mobile.luka.data.local.dao.DemandDao
import elieoko.mobile.luka.data.local.dao.MetaDao
import elieoko.mobile.luka.data.local.dao.NewsDao
import elieoko.mobile.luka.data.local.dao.OfferDao
import elieoko.mobile.luka.data.local.dao.OrientationDao
import elieoko.mobile.luka.data.local.dao.ProfessionalDao
import elieoko.mobile.luka.data.local.entity.AdEntity
import elieoko.mobile.luka.data.local.entity.DemandStatEntity
import elieoko.mobile.luka.data.local.entity.MetaEntity
import elieoko.mobile.luka.data.local.entity.NewsEntity
import elieoko.mobile.luka.data.local.entity.OfferEntity
import elieoko.mobile.luka.data.local.entity.OrientationEntity
import elieoko.mobile.luka.data.local.entity.ProfessionalEntity

@Database(
    entities = [
        OfferEntity::class,
        AdEntity::class,
        NewsEntity::class,
        ProfessionalEntity::class,
        OrientationEntity::class,
        DemandStatEntity::class,
        MetaEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(LukaDatabaseConstructor::class)
abstract class LukaDatabase : RoomDatabase() {
    abstract fun offerDao(): OfferDao
    abstract fun adDao(): AdDao
    abstract fun newsDao(): NewsDao
    abstract fun professionalDao(): ProfessionalDao
    abstract fun orientationDao(): OrientationDao
    abstract fun demandDao(): DemandDao
    abstract fun metaDao(): MetaDao
}

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object LukaDatabaseConstructor : RoomDatabaseConstructor<LukaDatabase> {
    override fun initialize(): LukaDatabase
}
