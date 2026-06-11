package com.germandebustamante.ringtonemanager.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.germandebustamante.ringtonemanager.data.local.db.entity.RingtoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RingtoneDao {

    @Query("SELECT * FROM ringtones ORDER BY popularity DESC")
    fun observeAll(): Flow<List<RingtoneEntity>>

    @Query("SELECT * FROM ringtones WHERE id = :id")
    suspend fun getById(id: String): RingtoneEntity?

    @Upsert
    suspend fun upsertAll(ringtones: List<RingtoneEntity>)

    @Upsert
    suspend fun upsert(ringtone: RingtoneEntity)
}
