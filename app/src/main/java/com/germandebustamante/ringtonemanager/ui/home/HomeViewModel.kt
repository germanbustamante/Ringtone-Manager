package com.germandebustamante.ringtonemanager.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetFullRingtonesUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getFullRingtonesUseCase: GetFullRingtonesUseCase,
) : BaseViewModel() {

    var state by mutableStateOf(UIState())
        private set

    init {
        getFullRingtones()
    }

    fun getFullRingtones() {
        viewModelScope.launch {
            getFullRingtonesUseCase().fold(
                { error ->
                    Log.e(TAG, "Error getting full ringtones${error.toErrorString()}")
                },
                { ringtones ->
                    state = state.copy(ringtones = ringtones)
                }
            )
        }
    }

    data class UIState(
        val ringtones: List<RingtoneBO> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    companion object {
        private const val TAG = "HomeViewModel"
    }
}