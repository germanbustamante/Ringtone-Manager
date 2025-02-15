package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import com.germandebustamante.ringtonemanager.utils.extensions.collectEither

class HomeViewModel(
    private val getPopularRingtonesUseCase: GetPopularRingtonesUseCase,
    private val player: MultiplePlayerAdapter,
    private val navigator: Navigator
) : BaseViewModel(navigator) {

    var state by mutableStateOf(UIState())
        private set

    init {
        getFullRingtones()
    }

    //region Public methods
    fun onPlayRingtoneClicked(ringtone: RingtoneBO, isPlaying: Boolean) {
        if (isPlaying) {
            pausePlayer()
        } else {
            player.play(ringtone.fileUrl)
            state = state.updateCurrentRingtonePlayingId(ringtone)
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
        state = state.copy(currentRingtonePlayingId = null)
    }
    //endregion

    //region Private methods
    private fun notifyLoading(loading: Boolean) {
        state = state.copy(isLoading = loading)
    }

    private fun getFullRingtones() {
        launchCatching(onError = { notifyError(it) }) {
            notifyLoading(true)
            getPopularRingtonesUseCase().collectEither(
                onLeft = { notifyError(it.toErrorString()) },
                onRight = { ringtones ->
                    player.addMediaItems(ringtones.map { it.fileUrl })
                    state = state.copy(ringtones = ringtones, isLoading = false)
                }
            )
        }
    }

    private fun notifyError(error: String) {
        state = state.copy(error = error, isLoading = false)
    }

    fun navigateToRingtoneDetail(ringtoneId: String) {
        navigateTo(Destination.RingtoneDetailScreen(ringtoneId))
    }
//endregion

    data class UIState(
        val ringtones: List<RingtoneBO> = emptyList(),
        val currentRingtonePlayingId: String? = null,
        val isLoading: Boolean = true,
        val error: String? = null,
    ) {
        fun updateCurrentRingtonePlayingId(ringtoneBO: RingtoneBO): UIState =
            copy(currentRingtonePlayingId = ringtones.firstOrNull { it.id == ringtoneBO.id }?.id)
    }
}