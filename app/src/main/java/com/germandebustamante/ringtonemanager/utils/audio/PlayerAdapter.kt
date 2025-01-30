package com.germandebustamante.ringtonemanager.utils.audio

interface MultiplePlayerAdapter {
    fun addMediaItems(mediaUris: List<String>)

    fun play(url: String)
    fun pause()
    fun release()
    fun restore()
}

interface SinglePlayerAdapter {

    fun addMediaItem(mediaUri: String)
    fun setListeners(
        onDurationReceived: (Int) -> Unit,
        onPositionChanged: (Long) -> Unit,
        onPlaybackEnded: () -> Unit,
    )

    fun play(url: String)
    fun pause()
    fun release()
    fun seekTo(position: Int)
    fun forward(timeInMillis: Long)
    fun rewind(timeInMillis: Long)
}