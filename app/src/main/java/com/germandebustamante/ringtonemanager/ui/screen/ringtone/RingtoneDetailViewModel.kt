package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.SinglePlayerAdapter

class RingtoneDetailViewModel(
    private val route: Destination.RingtoneDetailScreen,
    private val playerAdapter: SinglePlayerAdapter,
    private val fetchRingtoneDetailUseCase: GetRingtoneDetailUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    var uiState by mutableStateOf(RingtoneDetailUIState())
        private set

    init {
        setupPlayerListeners()
        fetchRingtoneDetails()
    }

    //region Public Methods
    /**
     * Handles the play/pause action for the ringtone.
     */
    fun onPlayPauseRingtone() {
        uiState.ringtone?.let { ringtone ->
            if (uiState.isPlaying) {
                playerAdapter.pause()
            } else {
                playerAdapter.play(ringtone.fileUrl)
            }
            uiState = uiState.copy(isPlaying = !uiState.isPlaying)
        }
    }

    /**
     * Updates the current playback position in the state.
     */
    fun updatePlaybackPosition(position: Int) {
        uiState = uiState.copy(currentPlaybackPosition = position)
    }

    //endregion

    //region Private Methods
    /**
     * Sets up listeners for playback duration and position changes.
     */
    private fun setupPlayerListeners() {
        playerAdapter.setListeners(
            onDurationReceived = { duration ->
                uiState = uiState.copy(ringtoneDuration = duration)
            },
            onPositionChanged = { position ->
                updatePlaybackPosition(position.toInt())
            },
            onPlaybackEnded = {
                uiState =
                    uiState.copy(isPlaying = false, currentPlaybackPosition = RingtoneDetailUIState.DEFAULT_DURATION)
            }
        )
    }

    /**
     * Fetches ringtone details using the ringtone ID from the saved state handle.
     */
    private fun fetchRingtoneDetails() {
        launchCatching(onError = { setErrorState(it) }) {
            setLoadingState(true)
            val ringtoneId = route.ringtoneId
            fetchRingtoneDetailUseCase(ringtoneId).fold(
                ifLeft = { error -> setErrorState(error) },
                ifRight = { ringtoneDetails ->
                    playerAdapter.addMediaItem(ringtoneDetails.fileUrl)
                    uiState = uiState.copy(ringtone = ringtoneDetails)
                    setLoadingState(false)
                }
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        uiState = uiState.copy(isLoading = isLoading)
    }

    private fun setErrorState(error: ErrorBO) {
        uiState = uiState.copy(error = error, isLoading = false)
    }

    fun onSeekButtonClick(timeInMillis: Int) {
        playerAdapter.seekTo(timeInMillis.toLong())
    }

    fun pausePlayer() {
        playerAdapter.pause()
    }

    fun releasePlayer() {
        playerAdapter.release()
    }
    //endregion

    data class RingtoneDetailUIState(
        val isLoading: Boolean = false,
        val error: ErrorBO? = null,
        val ringtone: RingtoneBO? = null,
        val ringtoneDuration: Int? = null,
        val currentPlaybackPosition: Int = DEFAULT_DURATION,
        val isPlaying: Boolean = false,
    ) {
        companion object {
            const val DEFAULT_DURATION = 0
        }
    }
}
