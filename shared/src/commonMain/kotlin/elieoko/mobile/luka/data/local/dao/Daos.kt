package elieoko.mobile.luka.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import elieoko.mobile.luka.data.local.entity.AdEntity
import elieoko.mobile.luka.data.local.entity.DemandStatEntity
import elieoko.mobile.luka.data.local.entity.MetaEntity
import elieoko.mobile.luka.data.local.entity.NewsEntity
import elieoko.mobile.luka.data.local.entity.OfferEntity
import elieoko.mobile.luka.data.local.entity.OrientationEntity
import elieoko.mobile.luka.data.local.entity.ProfessionalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers ORDER BY postedAtEpochMs DESC")
    fun observeAll(): Flow<List<OfferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<OfferEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: OfferEntity)

    @Query("SELECT COUNT(*) FROM offers")
    suspend fun count(): Int

    @Query("DELETE FROM offers")
    suspend fun clear()
}

@Dao
interface AdDao {
    @Query("SELECT * FROM ads")
    fun observeAll(): Flow<List<AdEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<AdEntity>)

    @Query("DELETE FROM ads")
    suspend fun clear()
}

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY publishedAtEpochMs DESC")
    fun observeAll(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<NewsEntity>)

    @Query("DELETE FROM news")
    suspend fun clear()
}

@Dao
interface ProfessionalDao {
    @Query("SELECT * FROM professionals")
    fun observeAll(): Flow<List<ProfessionalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<ProfessionalEntity>)

    @Query("DELETE FROM professionals")
    suspend fun clear()
}

@Dao
interface OrientationDao {
    @Query("SELECT * FROM orientation ORDER BY demandScore DESC")
    fun observeAll(): Flow<List<OrientationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<OrientationEntity>)

    @Query("DELETE FROM orientation")
    suspend fun clear()
}

@Dao
interface DemandDao {
    @Query("SELECT * FROM demand_stats ORDER BY openings DESC")
    fun observeAll(): Flow<List<DemandStatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(items: List<DemandStatEntity>)

    @Query("DELETE FROM demand_stats")
    suspend fun clear()
}

@Dao
interface MetaDao {
    @Query("SELECT * FROM meta WHERE `key` = :key LIMIT 1")
    suspend fun get(key: String): MetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(entity: MetaEntity)
}
