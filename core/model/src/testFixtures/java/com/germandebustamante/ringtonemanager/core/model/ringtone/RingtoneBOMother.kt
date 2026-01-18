package com.germandebustamante.ringtonemanager.core.model.ringtone

object RingtoneBOMother {

    fun default() = RingtoneBO(
        id = "ringtone_1",
        name = "Cool Ringtone",
        artist = "Famous Artist",
        source = "Ringtone Source",
        fileUrl = "https://example.com/ringtone_01.mp3",
        popularity = 1000,
    )

    fun random(id: String = "1") = RingtoneBO(
        id = "ringtone_$id",
        name = "Ringtone ${(10..99).random()}",
        artist = "Artist ${(10..99).random()}",
        source = "Source ${(10..99).random()}",
        fileUrl = "https://example.com/ringtone_${(10..99).random()}.mp3",
        popularity = (0..5000).random(),
    )

    fun randomList(size: Int = 4): List<RingtoneBO> = List(size) { random(it.toString()) }

}