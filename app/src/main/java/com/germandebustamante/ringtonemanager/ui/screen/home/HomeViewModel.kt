package com.germandebustamante.ringtonemanager.ui.screen.home

import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import com.germandebustamante.ringtonemanager.core.navigation.action.Navigator
import com.germandebustamante.ringtonemanager.core.navigation.destination.Destination
import com.germandebustamante.ringtonemanager.domain.authorization.usecase.GetUserFlowUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.LoadMoreRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.ObserveFavoriteIdsUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.SyncPopularRingtonesUseCase
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.ToggleFavoriteUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import com.germandebustamante.ringtonemanager.utils.audio.MultiplePlayerAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Suppress("LongParameterList")
class HomeViewModel(
    initialState: UIState = UIState(),
    private val getPopularRingtonesUseCase: GetPopularRingtonesUseCase,
    private val syncPopularRingtonesUseCase: SyncPopularRingtonesUseCase,
    private val loadMoreRingtonesUseCase: LoadMoreRingtonesUseCase,
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val observeFavoriteIdsUseCase: ObserveFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val player: MultiplePlayerAdapter,
    navigator: Navigator,
) : BaseViewModel(navigator) {

    private val _state: MutableStateFlow<UIState> = MutableStateFlow(initialState)
    val state: StateFlow<UIState> = _state

    fun start() {
        observeRingtones()
        syncRingtones()
        observeUser()
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

    fun loadMoreRingtones() {
        if (_state.value.isLoadingMore || !_state.value.canLoadMore) return
        launchCatching(onError = { error -> _state.update { it.copy(isLoadingMore = false, error = error) } }) {
            _state.update { it.copy(isLoadingMore = true) }
            loadMoreRingtonesUseCase()
                .onRight { hasMore -> _state.update { it.copy(isLoadingMore = false, canLoadMore = hasMore) } }
                .onLeft { error -> _state.update { it.copy(isLoadingMore = false, error = error) } }
        }
    }

    fun toggleFavorite(ringtoneId: String) {
        val userId = _state.value.userId ?: return
        val isFavorite = ringtoneId in _state.value.favoriteIds
        launchCatching(onError = {}) {
            toggleFavoriteUseCase(userId, ringtoneId, isFavorite)
        }
    }

    fun navigateToRingtoneDetail(ringtoneId: String) {
        navigateTo(Destination.RingtoneDetailScreen(ringtoneId))
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

    private fun observeUser() {
        launchCatching(onError = {}) {
            getUserFlowUseCase().collect { result ->
                val userId = result.getOrNull()?.id
                _state.update { it.copy(userId = userId) }
                if (userId != null) {
                    observeFavorites(userId)
                } else {
                    _state.update { it.copy(favoriteIds = emptySet()) }
                }
            }
        }
    }

    private fun observeFavorites(userId: String) {
        launchCatching(onError = {}) {
            observeFavoriteIdsUseCase(userId).collect { result ->
                result.onRight { ids -> _state.update { it.copy(favoriteIds = ids) } }
            }
        }
    }

    private fun notifyError(error: ErrorBO) {
        _state.update { it.copy(error = error, isLoading = false) }
    }
    //endregion

    data class UIState(
        val ringtones: List<RingtoneBO> = emptyList(),
        val favoriteIds: Set<String> = emptySet(),
        val userId: String? = null,
        val currentRingtonePlayingId: String? = null,
        val isLoading: Boolean = true,
        val isLoadingMore: Boolean = false,
        val canLoadMore: Boolean = true,
        val error: ErrorBO? = null,
    ) {
        val isLoggedIn: Boolean get() = userId != null
        val favoriteRingtones: List<RingtoneBO> get() = ringtones.filter { it.id in favoriteIds }

        fun updateCurrentRingtonePlayingId(ringtoneBO: RingtoneBO): UIState =
            copy(currentRingtonePlayingId = ringtones.firstOrNull { it.id == ringtoneBO.id }?.id)
    }
}
