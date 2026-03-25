package com.germandebustamante.ringtonemanager.core.model.ringtone

data class RingtoneBO(
    val id: String,
    val name: String,
    val artist: String?,
    val source: String?,
    val fileUrl: String,
    val popularity: Int,
)