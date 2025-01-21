package com.germandebustamante.ringtonemanager.ui.ringtone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.germandebustamante.ringtonemanager.core.navigation.bottom.screen.RingtoneDetailScreen

class RingtoneDetailViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    val ringtoneId = savedStateHandle.toRoute<RingtoneDetailScreen>().ringtoneId
}