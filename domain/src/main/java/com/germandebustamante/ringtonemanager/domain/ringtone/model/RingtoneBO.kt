package com.germandebustamante.ringtonemanager.domain.ringtone.model

data class RingtoneBO (
    val id: String,
    val name: String,
    val artist: String?,
    val source: String?,
    val fileUrl: String,
)