package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.IncrementRingtonePopularityUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.SinglePlayerAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RingtoneDetailViewModel(
    private val route: Destination.RingtoneDetailScreen,
    private val playerAdapter: SinglePlayerAdapter,
    private val fetchRingtoneDetailUseCase: GetRingtoneDetailUseCase,
    private val incrementRingtonePopularityUseCase: IncrementRingtonePopularityUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _uiState = MutableStateFlow(RingtoneDetailUIState())
    val uiState: StateFlow<RingtoneDetailUIState> = _uiState

    init {
        setupPlayerListeners()
        fetchRingtoneDetails()
    }

    //region Public Methods
    /**
     * Handles the play/pause action for the ringtone.
     */
    fun onPlayPauseRingtone() {
        _uiState.value.ringtone?.let { ringtone ->
            if (_uiState.value.isPlaying) {
                playerAdapter.pause()
            } else {
                playerAdapter.play(ringtone.fileUrl)
            }
            _uiState.update { it.copy(isPlaying = !it.isPlaying) }
        }
    }

    /**
     * Updates the current playback position in the state.
     */
    fun updatePlaybackPosition(position: Int) {
        _uiState.update { it.copy(currentPlaybackPosition = position) }
    }

    //endregion

    //region Private Methods
    /**
     * Sets up listeners for playback duration and position changes.
     */
    private fun setupPlayerListeners() {
        playerAdapter.setListeners(
            onDurationReceived = { duration ->
                _uiState.update { it.copy(ringtoneDuration = duration) }
            },
            onPositionChanged = { position ->
                updatePlaybackPosition(position.toInt())
            },
            onPlaybackEnded = {
                _uiState.update {
                    it.copy(isPlaying = false, currentPlaybackPosition = RingtoneDetailUIState.DEFAULT_DURATION)
                }
            }
        )
    }

    /**
     * Fetches ringtone details using the ringtone ID from the saved state handle.
     */
    private fun fetchRingtoneDetails() {
        launchCatching(onError = { setErrorState(it) }) {
            setLoadingState(true)
            fetchRingtoneDetailUseCase(route.ringtoneId).fold(
                ifLeft = { error -> setErrorState(error) },
                ifRight = { ringtoneDetails ->
                    playerAdapter.addMediaItem(ringtoneDetails.fileUrl)
                    _uiState.update { it.copy(ringtone = ringtoneDetails) }
                    setLoadingState(false)
                    incrementRingtonePopularityUseCase(ringtoneDetails.id)
                }
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun setErrorState(error: ErrorBO) {
        _uiState.update { it.copy(error = error, isLoading = false) }
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
