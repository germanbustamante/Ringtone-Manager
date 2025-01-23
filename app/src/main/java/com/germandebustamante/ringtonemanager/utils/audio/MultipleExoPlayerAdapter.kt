package com.germandebustamante.ringtonemanager.utils.audio

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.germandebustamante.ringtonemanager.utils.extensions.isTrue
import java.lang.ref.WeakReference

class MultipleExoPlayerAdapter(
    private val context: Context,
) : MultiplePlayerAdapter {

    private var player: ExoPlayer? = null
    private var playerIndex: Int = NO_MEDIA_ITEM_SELECTED
    private var mediaItems = emptyList<MediaItem>()
    private val positionUpdateHandler = Handler()
    private val positionUpdateRunnable = object : Runnable {
        override fun run() {
            positionUpdateHandler.postDelayed(this, PLAYBACK_POSITION_REFRESH_INTERVAL_MS)
        }
    }

    init {
        initializeExoPlayer()
    }

    override fun play(url: String) {
        val mediaItemToPlay = MediaItem.fromUri(Uri.parse(url))

        if (player?.currentMediaItem == mediaItemToPlay && player?.playbackState != Player.STATE_ENDED) {
            player?.play()
        } else {
            val mediaItemPosition = mediaItems.indexOf(mediaItemToPlay)
            player?.seekTo(mediaItemPosition, C.TIME_UNSET)
            player?.play()
        }
    }

    override fun pause() {
        if (player?.isPlaying.isTrue()) {
            player?.pause()
            positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
        }
    }

    override fun release() {
        player?.release()
        positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
    }

    @OptIn(UnstableApi::class)
    override fun restore() {
        if (player?.isReleased.isTrue()) {
            initializeExoPlayer()
            player?.addMediaItems(mediaItems)
            player?.prepare()

        }
    }

    override fun addMediaItems(mediaUris: List<String>) {
        val mediaItems = mediaUris.map { MediaItem.fromUri(it) }
        this.mediaItems = mediaItems
        player?.addMediaItems(mediaItems)
        player?.prepare()
    }

    @OptIn(UnstableApi::class)
    private fun initializeExoPlayer() {
        WeakReference(context).get()?.let {
            player = ExoPlayer.Builder(it).build()
            player?.pauseAtEndOfMediaItems = true
            player?.addListener((object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    logError("ExoPlayer error: $error")
                }

                @SuppressLint("SwitchIntDef")
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)

                    when (playbackState) {
                        Player.STATE_ENDED -> {
                            player?.pause()
                            positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
                        }

                        Player.STATE_READY -> {
                            player?.seekTo(playerIndex, C.TIME_UNSET)
                        }
                    }
                }
            }))
        }
    }

    private fun logError(error: String?) {
        Log.e(TAG, "MediaPlayerHolder error: $error")
    }

    companion object {
        private const val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 1000L
        private const val NO_MEDIA_ITEM_SELECTED = -1
        private const val TAG = "ExoPlayerAdapter"
    }
}