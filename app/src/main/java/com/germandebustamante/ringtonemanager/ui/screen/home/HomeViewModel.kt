package com.germandebustamante.ringtonemanager.ui.screen.home

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.SyncPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getPopularRingtonesUseCase: GetPopularRingtonesUseCase,
    private val syncPopularRingtonesUseCase: SyncPopularRingtonesUseCase,
    private val player: MultiplePlayerAdapter,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _state: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val state: StateFlow<UIState> = _state

    init {
        observeRingtones()
        syncRingtones()
    }

    //region Public methods
    fun onPlayRingtoneClicked(ringtone: RingtoneBO, isPlaying: Boolean) {
        if (isPlaying) {
            pausePlayer()
        } else {
            player.play(ringtone.fileUrl)
            _state.update { it.updateCurrentRingtonePlayingId(ringtone) }
        }
    }

    fun releasePlayer() {
        player.release()
    }

    fun restorePlayer() {
        player.restore()
    }

    fun pausePlayer() {
        player.pause()
        _state.update { it.copy(currentRingtonePlayingId = null) }
    }
    //endregion

    //region Private methods
    private fun observeRingtones() {
        launchCatching(onError = { notifyError(it) }) {
            getPopularRingtonesUseCase().collect { ringtones ->
                player.addMediaItems(ringtones.map { it.fileUrl })
                _state.update { it.copy(ringtones = ringtones) }
            }
        }
    }

    private fun syncRingtones() {
        launchCatching(onError = { error -> _state.update { it.copy(isLoading = false, error = error) } }) {
            val result = syncPopularRingtonesUseCase()
            _state.update { it.copy(isLoading = false, error = result.leftOrNull()) }
        }
    }

    private fun notifyError(error: ErrorBO) {
        _state.update { it.copy(error = error, isLoading = false) }
    }

    fun navigateToRingtoneDetail(ringtoneId: String) {
        navigateTo(Destination.RingtoneDetailScreen(ringtoneId))
    }
    //endregion

    data class UIState(
        val ringtones: List<RingtoneBO> = emptyList(),
        val currentRingtonePlayingId: String? = null,
        val isLoading: Boolean = true,
        val error: ErrorBO? = null,
    ) {
        fun updateCurrentRingtonePlayingId(ringtoneBO: RingtoneBO): UIState =
            copy(currentRingtonePlayingId = ringtones.firstOrNull { it.id == ringtoneBO.id }?.id)
    }
}
