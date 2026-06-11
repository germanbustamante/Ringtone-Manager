package com.germandebustamante.ringtonemanager.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.germandebustamante.ringtonemanager.data.local.db.dao.RingtoneDao
import com.germandebustamante.ringtonemanager.data.local.db.entity.RingtoneEntity

@Database(entities = [RingtoneEntity::class], version = 1, exportSchema = false)
abstract class RingtoneDatabase : RoomDatabase() {
    abstract fun ringtoneDao(): RingtoneDao

    companion object {
        const val DATABASE_NAME = "ringtone_manager.db"
    }
}
