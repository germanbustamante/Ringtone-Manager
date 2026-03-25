package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO

class HomePreviewParameterProviders : PreviewParameterProvider<HomeViewModel.UIState> {

    override val values: Sequence<HomeViewModel.UIState>
        get() = sequenceOf(
            HomeViewModel.UIState(
                ringtones = listOf(
                    RingtoneBO(
                        id = "1",
                        name = "Bella Ciao",
                        artist = "Maneskin",
                        source = null,
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "2",
                        name = "Quiero ser",
                        artist = "Mago de Oz",
                        source = "La Casa de Papel",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "3",
                        name = "Sintiéndolo Mucho",
                        artist = "Los Secretos",
                        source = null,
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "4",
                        name = "Toca Toca",
                        artist = "Fly Project",
                        source = "La Casa de las Flores",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "5",
                        name = "Alma de cantautor",
                        artist = "José Antonio Ramos Sucre",
                        source = "Las Chicas del Cable",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "6",
                        name = "A lo lejos",
                        artist = "La India",
                        source = "Vis a Vis",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "7",
                        name = "El Regalo de la Diosa",
                        artist = "Soleá Morente",
                        source = null,
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "8",
                        name = "Quédate",
                        artist = "Sebastián Yatra",
                        source = "Élite",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "9",
                        name = "Merlí",
                        artist = "Santi Balmes",
                        source = "Merlí",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "10",
                        name = "La vida es un carnaval",
                        artist = "Celia Cruz",
                        source = "La Casa de las Flores",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "11",
                        name = "Canción del adiós",
                        artist = "Los Secretos",
                        source = "Cuentame cómo pasó",
                        fileUrl = "",
                        popularity = 0
                    ),
                    RingtoneBO(
                        id = "12",
                        name = "Deja que te bese",
                        artist = "Carlos Vives & Shakira",
                        source = null,
                        fileUrl = "",
                        popularity = 0
                    ),
                )
            )
        )
}
