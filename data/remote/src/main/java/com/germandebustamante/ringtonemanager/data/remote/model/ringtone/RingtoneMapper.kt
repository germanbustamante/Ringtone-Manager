package com.germandebustamante.ringtonemanager.data.remote.model.ringtone

import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

fun RingtoneDTO.toDomain(): RingtoneBO = RingtoneBO(
    id = id.toString(),
    name = name.orEmpty(),
    artist = artist,
    source = source,
    fileUrl = file_url.orEmpty(),
    popularity = popularity ?: 0
)