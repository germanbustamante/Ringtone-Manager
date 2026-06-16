package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.IncrementRingtonePopularityUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.ObserveFavoriteIdsUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.ToggleFavoriteUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.SinglePlayerAdapter
import com.germandebustamante.ringtonemanager.utils.ringtone.RingtoneInstaller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Suppress("LongParameterList")
class RingtoneDetailViewModel(
    private val route: Destination.RingtoneDetailScreen,
    private val playerAdapter: SinglePlayerAdapter,
    private val fetchRingtoneDetailUseCase: GetRingtoneDetailUseCase,
    private val incrementRingtonePopularityUseCase: IncrementRingtonePopularityUseCase,
    private val ringtoneInstaller: RingtoneInstaller,
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val observeFavoriteIdsUseCase: ObserveFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _uiState = MutableStateFlow(RingtoneDetailUIState())
    val uiState: StateFlow<RingtoneDetailUIState> = _uiState

    init {
        setupPlayerListeners()
        fetchRingtoneDetails()
        observeUser()
    }

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

    fun updatePlaybackPosition(position: Int) {
        _uiState.update { it.copy(currentPlaybackPosition = position) }
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

    fun setAsRingtone() {
        val ringtone = _uiState.value.ringtone ?: return
        launchCatching(onError = { error -> _uiState.update { it.copy(isSettingRingtone = false, error = error) } }) {
            _uiState.update { it.copy(isSettingRingtone = true, isRingtoneSet = false) }
            ringtoneInstaller.install(ringtone)
                .onRight { _uiState.update { it.copy(isSettingRingtone = false, isRingtoneSet = true) } }
                .onLeft { error -> _uiState.update { it.copy(isSettingRingtone = false, error = error) } }
        }
    }

    fun toggleFavorite() {
        val userId = _uiState.value.userId ?: return
        val ringtoneId = _uiState.value.ringtone?.id ?: return
        val isFavorite = _uiState.value.isFavorite
        launchCatching(onError = {}) {
            toggleFavoriteUseCase(userId, ringtoneId, isFavorite)
        }
    }

    private fun observeUser() {
        launchCatching(onError = {}) {
            getUserFlowUseCase().collect { result ->
                val userId = result.getOrNull()?.id
                _uiState.update { it.copy(userId = userId) }
                if (userId != null) {
                    observeFavoriteIds(userId)
                }
            }
        }
    }

    private fun observeFavoriteIds(userId: String) {
        launchCatching(onError = {}) {
            observeFavoriteIdsUseCase(userId).collect { result ->
                result.onRight { ids ->
                    _uiState.update { it.copy(isFavorite = route.ringtoneId in ids) }
                }
            }
        }
    }

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
            },
        )
    }

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
                },
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun setErrorState(error: ErrorBO) {
        _uiState.update { it.copy(error = error, isLoading = false) }
    }

    data class RingtoneDetailUIState(
        val isLoading: Boolean = false,
        val isSettingRingtone: Boolean = false,
        val isRingtoneSet: Boolean = false,
        val isFavorite: Boolean = false,
        val userId: String? = null,
        val error: ErrorBO? = null,
        val ringtone: RingtoneBO? = null,
        val ringtoneDuration: Int? = null,
        val currentPlaybackPosition: Int = DEFAULT_DURATION,
        val isPlaying: Boolean = false,
    ) {
        val isLoggedIn: Boolean get() = userId != null

        companion object {
            const val DEFAULT_DURATION = 0
        }
    }
}
