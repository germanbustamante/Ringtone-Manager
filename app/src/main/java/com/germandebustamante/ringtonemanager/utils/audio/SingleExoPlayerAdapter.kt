//package com.germandebustamante.ringtonemanager.utils.audio
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.net.Uri
//import android.os.Handler
//import android.util.Log
//import androidx.media3.common.MediaItem
//import androidx.media3.common.PlaybackException
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
//import com.germandebustamante.ringtonemanager.utils.extensions.isTrue
//import java.lang.ref.WeakReference
//
//class SingleExoPlayerAdapter(
//    context: Context,
//) : PlayerAdapter {
//
//    private var player: ExoPlayer? = null
//    private val positionUpdateHandler = Handler()
//    private val positionUpdateRunnable = object : Runnable {
//        override fun run() {
//            updatePosition(getPlayerCurrentPosition().toInt())
//            positionUpdateHandler.postDelayed(this, PLAYBACK_POSITION_REFRESH_INTERVAL_MS)
//        }
//    }
//
//    init {
//        WeakReference(context).get()?.let {
//            player = ExoPlayer.Builder(it).build()
//            initializeExoPlayer()
//        }
//    }
//
//    override fun play(url: String) {
//        val mediaItem = MediaItem.fromUri(Uri.parse(url))
//
//        if (player?.currentMediaItem == mediaItem && player?.playbackState != Player.STATE_ENDED) { //User try to play the same audio has not ended
//            play()
//        } else { //User try to play another audio || First time playing || User try to play the same audio has ended
//            player?.setMediaItem(mediaItem, true)
//            player?.prepare()
//        }
//    }
//
//    override fun pause() {
//        if (player?.isPlaying.isTrue()) {
//            player?.pause()
//            positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
//        }
//    }
//
//    override fun release() {
//        player?.pause()
//        player?.stop()
//        player?.release()
//        positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
//    }
//
//    override fun seekTo(position: Int) {
//        player?.seekTo(position.toLong())
//    }
//
//    override fun forward(timeInMillis: Long) {
//        player?.seekTo(getPlayerCurrentPosition() + timeInMillis)
//    }
//
//    override fun rewind(timeInMillis: Long) {
//        player?.seekTo(getPlayerCurrentPosition() - timeInMillis)
//    }
//
//    private fun initializeExoPlayer() {
//        player?.addListener((object : Player.Listener {
//            override fun onPlayerError(error: PlaybackException) {
//                logError("ExoPlayer error: $error")
//            }
//
//            @SuppressLint("SwitchIntDef")
//            override fun onPlaybackStateChanged(playbackState: Int) {
//                super.onPlaybackStateChanged(playbackState)
//
//                when (playbackState) {
//                    Player.STATE_ENDED -> {
//                        positionUpdateHandler.removeCallbacks(positionUpdateRunnable)
////                        playbackInfoListener.onStateChanged(PlaybackInfoListener.State.COMPLETED)
//                    }
//
//                    Player.STATE_READY -> {
//                        updateDuration()
//                        play()
//                    }
//                }
//            }
//        }))
//    }
//
//    private fun play() {
//        positionUpdateHandler.post(positionUpdateRunnable)
//        player?.play()
//    }
//
//    private fun updateDuration() {
//        val duration = (player?.duration ?: DEFAULT_DURATION).toInt()
//
//    }
//
//    private fun updatePosition(position: Int = DEFAULT_DURATION.toInt()) {
////        playbackInfoListener.onPositionChanged(position)
//    }
//
//    private fun logError(error: String?) {
//        Log.e(TAG, "SingleExoPlayerAdapter error: $error")
//    }
//
//    private fun getPlayerCurrentPosition(): Long = player?.currentPosition ?: DEFAULT_DURATION
//
//    companion object {
//        private const val PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 100L
//        const val DEFAULT_DURATION = 0L
//        private const val TAG = "SingleExoPlayerAdapter"
//    }
//}