package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

class RingtonePreviewParametersProviders : PreviewParameterProvider<RingtoneDetailViewModel.RingtoneDetailUIState> {

    override val values: Sequence<RingtoneDetailViewModel.RingtoneDetailUIState> =
        sequenceOf(
            RingtoneDetailViewModel.RingtoneDetailUIState(
                ringtone = RingtoneBO(
                    "1",
                    "Bella Ciao",
                    artist = "Maneskin",
                    source = "La Casa de Papel",
                    fileUrl = "https://www.example.com/bellaciao.mp3"
                )
            )
        )
}