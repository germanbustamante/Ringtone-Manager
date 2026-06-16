package com.germandebustamante.ringtonemanager.data.local.db.mapper

import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.data.local.db.entity.RingtoneEntity

fun RingtoneEntity.toDomain() = RingtoneBO(
    id = id,
    name = name,
    artist = artist,
    source = source,
    fileUrl = fileUrl,
    popularity = popularity,
)

fun RingtoneBO.toEntity() = RingtoneEntity(
    id = id,
    name = name,
    artist = artist,
    source = source,
    fileUrl = fileUrl,
    popularity = popularity,
)
