package com.germandebustamante.ringtonemanager.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ringtones")
data class RingtoneEntity(
    @PrimaryKey val id: String,
    val name: String,
    val artist: String?,
    val source: String?,
    @ColumnInfo(name = "file_url") val fileUrl: String,
    val popularity: Int,
)
