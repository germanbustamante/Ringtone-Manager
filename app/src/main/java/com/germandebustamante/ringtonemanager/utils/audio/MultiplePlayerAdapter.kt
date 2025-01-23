package com.germandebustamante.ringtonemanager.utils.audio

interface MultiplePlayerAdapter {
    fun addMediaItems(mediaUris: List<String>)

    fun play(url: String)
    fun pause()
    fun release()
    fun restore()
}