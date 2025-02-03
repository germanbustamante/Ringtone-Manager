package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetFullRingtonesUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFullRingtonesUseCase: GetFullRingtonesUseCase,
    private val player: MultiplePlayerAdapter
) : BaseViewModel() {

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
        viewModelScope.launch {
            notifyLoading(true)
            getFullRingtonesUseCase().fold(
                ifLeft = {
                    notifyError(it.toErrorString())
                },
                ifRight = { ringtones ->
                    notifyLoading(false)

                    player.addMediaItems(ringtones.map { it.fileUrl })
                    state = state.copy(ringtones = ringtones)
                }
            )
        }
    }

    private fun notifyError(error: String) {
        state = state.copy(error = error, isLoading = false)
    }
    //endregion

    data class UIState(
        val ringtones: List<RingtoneBO> = emptyList(),
        val currentRingtonePlayingId: Int? = null,
        val isLoading: Boolean = true,
        val error: String? = null,
    ) {
        fun updateCurrentRingtonePlayingId(ringtoneBO: RingtoneBO): UIState =
            copy(currentRingtonePlayingId = ringtones.firstOrNull { it.id == ringtoneBO.id }?.id?.toIntOrNull())
    }
}