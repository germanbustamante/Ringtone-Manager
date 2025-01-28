package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.RingtoneDetailScreen
import com.germandebustamante.ringtonemanager.domain.ringtone.usecase.GetRingtoneDetailUseCase
import com.germandebustamante.ringtonemanager.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class RingtoneDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getRingtoneDetailUseCase: GetRingtoneDetailUseCase,
) : BaseViewModel() {

    val ringtoneId = savedStateHandle.toRoute<RingtoneDetailScreen>().ringtoneId

    init {
        getRingtoneDetail()
    }

    private fun getRingtoneDetail() {
        viewModelScope.launch {
            getRingtoneDetailUseCase(ringtoneId).fold(
                ifLeft = { error -> println("Error: ${error.toErrorString()}") },
                ifRight = { ringtone -> println("Ringtone: $ringtone") }
            )
        }
    }

}