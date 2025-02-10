package com.germandebustamante.ringtonemanager.data.remote.model.ringtone

import com.google.firebase.firestore.DocumentId

data class RingtoneDTO (
    @DocumentId
    val id: String? = null,
    val name: String? = null,
    val artist: String? = null,
    val source: String? = null,
    val file_url: String? = null,
    val popularity: Int? = null,
)