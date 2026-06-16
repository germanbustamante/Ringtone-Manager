package com.germandebustamante.ringtonemanager.utils.audio

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.germandebustamante.ringtonemanager.utils.extensions.isTrue
import java.lang.ref.WeakReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SingleExoPlayerAdapter(
    context: Context,
) : SinglePlayerAdapter {

    private var exoPlayer: ExoPlayer? = null
    private var onPlaybackPositionChanged: ((Long) -> Unit)? = null
    private var onPlaybackDurationChanged: ((Int) -> Unit)? = null
    private var onPlaybackEnded: (() -> Unit)? = null

    // Dedicated scope so we can cancel the position loop without touching any ViewModel scope.
    private var positionScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var positionJob: Job? = null

    init {
        WeakReference(context).get()?.let {
            exoPlayer = ExoPlayer.Builder(it)
                .setAudioAttributes(RINGTONE_AUDIO_ATTRIBUTES, true)
                .setHandleAudioBecomingNoisy(true)
                .build()
            setupPlayerListeners()
        }
    }

    override fun addMediaItem(mediaUri: String) {
        exoPlayer?.setMediaItem(MediaItem.fromUri(mediaUri))
        exoPlayer?.prepare()
    }

    override fun setListeners(
        onDurationReceived: (Int) -> Unit,
        onPositionChanged: (Long) -> Unit,
        onPlaybackEnded: () -> Unit,
    ) {
        onPlaybackDurationChanged = onDurationReceived
        onPlaybackPositionChanged = onPositionChanged
        this.onPlaybackEnded = onPlaybackEnded
    }

    override fun play(url: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(url))

        if (exoPlayer?.currentMediaItem == mediaItem && exoPlayer?.playbackState != Player.STATE_ENDED) {
            resumePlayback()
        } else {
            exoPlayer?.setMediaItem(mediaItem, true)
            exoPlayer?.prepare()
        }
    }

    override fun pause() {
        if (exoPlayer?.isPlaying.isTrue()) {
            exoPlayer?.pause()
            stopPositionUpdates()
        }
    }

    override fun release() {
        exoPlayer?.pause()
        exoPlayer?.stop()
        exoPlayer?.release()
        stopPositionUpdates()
    }

    override fun seekTo(position: Long) {
        exoPlayer?.seekTo(getCurrentPlaybackPosition() + position)
    }

    private fun resumePlayback() {
        exoPlayer?.play()
        startPositionUpdates()
    }

    private fun startPositionUpdates() {
        positionJob?.cancel()
        positionJob = positionScope.launch {
            while (isActive) {
                onPlaybackPositionChanged?.invoke(getCurrentPlaybackPosition())
                delay(PLAYBACK_POSITION_REFRESH_INTERVAL_MS)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionJob?.cancel()
        positionJob = null
    }

    private fun setupPlayerListeners() {
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e(TAG, "ExoPlayer error: $error")
            }

            @SuppressLint("SwitchIntDef")
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        stopPositionUpdates()
                        onPlaybackPositionChanged?.invoke(DEFAULT_DURATION)
                        onPlaybackEnded?.invoke()
                    }

                    Player.STATE_READY -> onPlaybackDurationChanged?.invoke(exoPlayer?.duration?.toInt() ?: 0)
                }
            }
        })
    }

    private fun getCurrentPlaybackPosition(): Long = exoPlayer?.currentPosition ?: DEFAULT_DURATION

    companion object {
        private const val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 50L
        private const val DEFAULT_DURATION = 0L
        private const val TAG = "SingleExoPlayerAdapter"

        private val RINGTONE_AUDIO_ATTRIBUTES = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
    }
}
