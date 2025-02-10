package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

class HomePreviewParameterProviders : PreviewParameterProvider<HomeViewModel.UIState> {

    override val values: Sequence<HomeViewModel.UIState>
        get() = sequenceOf(
            HomeViewModel.UIState(
                ringtones = listOf(
                    RingtoneBO.EMPTY.copy(
                        name = "Bella Ciao",
                        artist = "Maneskin",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Quiero ser",
                        artist = "Mago de Oz",
                        source = "La Casa de Papel",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Sintiéndolo Mucho",
                        artist = "Los Secretos",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Toca Toca",
                        artist = "Fly Project",
                        source = "La Casa de las Flores",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Alma de cantautor",
                        artist = "José Antonio Ramos Sucre",
                        source = "Las Chicas del Cable",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "A lo lejos",
                        artist = "La India",
                        source = "Vis a Vis",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "El Regalo de la Diosa",
                        artist = "Soleá Morente",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Quédate",
                        artist = "Sebastián Yatra",
                        source = "Élite",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Merlí",
                        artist = "Santi Balmes",
                        source = "Merlí",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "La vida es un carnaval",
                        artist = "Celia Cruz",
                        source = "La Casa de las Flores",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Canción del adiós",
                        artist = "Los Secretos",
                        source = "Cuentame cómo pasó",
                    ),
                    RingtoneBO.EMPTY.copy(
                        name = "Deja que te bese",
                        artist = "Carlos Vives & Shakira",
                    ),
                )
            )
        )
}