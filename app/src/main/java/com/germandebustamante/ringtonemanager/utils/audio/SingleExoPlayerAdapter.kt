package com.germandebustamante.ringtonemanager.utils.audio

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.germandebustamante.ringtonemanager.utils.extensions.isTrue
import java.lang.ref.WeakReference

/**
 * Adapter to manage a single instance of ExoPlayer for audio playback.
 */
class SingleExoPlayerAdapter(
    context: Context,
) : SinglePlayerAdapter {

    private var exoPlayer: ExoPlayer? = null
    private var onPlaybackPositionChanged: ((Long) -> Unit)? = null
    private var onPlaybackDurationChanged: ((Int) -> Unit)? = null
    private var onPlaybackEnded: (() -> Unit)? = null
    private val positionUpdateHandler = Handler()
    private val positionUpdateRunnable = object : Runnable {
        override fun run() {
            onPlaybackPositionChanged?.invoke(getCurrentPlaybackPosition())
            positionUpdateHandler.postDelayed(this, PLAYBACK_POSITION_REFRESH_INTERVAL_MS)
        }
    }

    init {
        // Initialize the ExoPlayer with a weak reference to context to avoid memory leaks.
        WeakReference(context).get()?.let {
            exoPlayer = ExoPlayer.Builder(it).build()
            setupPlayerListeners()
        }
    }

    /**
     * Adds a media item to the player and prepares it for playback.
     */
    override fun addMediaItem(mediaUri: String) {
        exoPlayer?.setMediaItem(MediaItem.fromUri(mediaUri))
        exoPlayer?.prepare()
    }

    /**
     * Sets listeners for playback duration, position and playback end events.
     */
    override fun setListeners(
        onDurationReceived: (Int) -> Unit,
        onPositionChanged: (Long) -> Unit,
        onPlaybackEnded: () -> Unit,
    ) {
        onPlaybackDurationChanged = onDurationReceived
        onPlaybackPositionChanged = onPositionChanged
        this.onPlaybackEnded = onPlaybackEnded
    }

    /**
     * Plays a media item from the specified URL. If the same media is already playing,
     * it resumes playback or starts over if the media has ended.
     */
    override fun play(url: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(url))

        if (exoPlayer?.currentMediaItem == mediaItem && exoPlayer?.playbackState != Player.STATE_ENDED) {
            resumePlayback()
        } else {
            exoPlayer?.setMediaItem(mediaItem, true)
            exoPlayer?.prepare()
        }
    }

    /**
     * Pauses playback and stops position updates.
     */
    override fun pause() {
        if (exoPlayer?.isPlaying.isTrue()) {
            exoPlayer?.pause()
            positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
        }
    }

    /**
     * Releases the player resources and stops all updates.
     */
    override fun release() {
        exoPlayer?.pause()
        exoPlayer?.stop()
        exoPlayer?.release()
        positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
    }

    /**
     * Seeks playback to a specific position in milliseconds.
     */
    override fun seekTo(position: Int) {
        exoPlayer?.seekTo(position.toLong())
    }

    /**
     * Moves playback forward by a specified duration in milliseconds.
     */
    override fun forward(timeInMillis: Long) {
        exoPlayer?.seekTo(getCurrentPlaybackPosition() + timeInMillis)
    }

    /**
     * Moves playback backward by a specified duration in milliseconds.
     */
    override fun rewind(timeInMillis: Long) {
        exoPlayer?.seekTo(getCurrentPlaybackPosition() - timeInMillis)
    }

    /**
     * Resumes playback and starts position updates.
     */
    private fun resumePlayback() {
        exoPlayer?.play()
        positionUpdateHandler.post(positionUpdateRunnable)
    }

    /**
     * Sets up ExoPlayer listeners to handle playback events and errors.
     */
    private fun setupPlayerListeners() {
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                logError("ExoPlayer error: $error")
            }

            @SuppressLint("SwitchIntDef")
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
                        onPlaybackPositionChanged?.invoke(DEFAULT_DURATION)
                        onPlaybackEnded?.invoke()
                    }
                    Player.STATE_READY -> onPlaybackDurationChanged?.invoke(exoPlayer?.duration?.toInt() ?: 0)
                }
            }
        })
    }

    /**
     * Logs an error message to the console.
     */
    private fun logError(message: String) {
        Log.e(TAG, "SingleExoPlayerAdapter error: $message")
    }

    /**
     * Gets the current playback position or a default value if unavailable.
     */
    private fun getCurrentPlaybackPosition(): Long = exoPlayer?.currentPosition ?: DEFAULT_DURATION

    companion object {
        private const val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 50L
        private const val DEFAULT_DURATION = 0L
        private const val TAG = "SingleExoPlayerAdapter"
    }
}