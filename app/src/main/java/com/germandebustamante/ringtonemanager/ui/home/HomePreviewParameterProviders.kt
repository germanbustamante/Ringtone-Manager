package com.germandebustamante.ringtonemanager.ui.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

class HomePreviewParameterProviders : PreviewParameterProvider<HomeViewModel.UIState> {

    override val values: Sequence<HomeViewModel.UIState>
        get() = sequenceOf(
            HomeViewModel.UIState(
                ringtones = listOf(
                    RingtoneBO("1", "Ringtone 1", "https://www.example.com/ringtone1.mp3"),
                    RingtoneBO("2", "Ringtone 2", "https://www.example.com/ringtone2.mp3"),
                    RingtoneBO("3", "Ringtone 3", "https://www.example.com/ringtone3.mp3"),
                    RingtoneBO("4", "Ringtone 4", "https://www.example.com/ringtone4.mp3"),
                    RingtoneBO("5", "Ringtone 5", "https://www.example.com/ringtone5.mp3"),
                    RingtoneBO("6", "Ringtone 6", "https://www.example.com/ringtone6.mp3"),
                    RingtoneBO("7", "Ringtone 7", "https://www.example.com/ringtone7.mp3"),
                    RingtoneBO("8", "Ringtone 8", "https://www.example.com/ringtone8.mp3"),
                    RingtoneBO("9", "Ringtone 9", "https://www.example.com/ringtone9.mp3"),
                    RingtoneBO("10", "Ringtone 10", "https://www.example.com/ringtone10.mp3"),
                    RingtoneBO("11", "Ringtone 11", "https://www.example.com/ringtone11.mp3"),
                    RingtoneBO("12", "Ringtone 12", "https://www.example.com/ringtone12.mp3"),
                    RingtoneBO("13", "Ringtone 13", "https://www.example.com/ringtone13.mp3"),
                )
            )
        )
}