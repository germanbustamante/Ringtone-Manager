package com.germandebustamante.ringtonemanager.utils.audio

import android.annotation.SuppressLint

object AudioUtils {

    private const val MINUTES_SECONDS_FORMAT = "%01d:%02d"

    @SuppressLint("DefaultLocale")
    fun getFormattedAudioDuration(progress: Int): String {
        val initialSeconds = progress / 1000
        val minutes = initialSeconds / 60
        val seconds = initialSeconds % 60
        return String.format(MINUTES_SECONDS_FORMAT, minutes, seconds)
    }
}