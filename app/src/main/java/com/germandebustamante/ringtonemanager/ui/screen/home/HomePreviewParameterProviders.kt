package com.germandebustamante.ringtonemanager.ui.screen.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.germandebustamante.ringtonemanager.domain.ringtone.model.RingtoneBO

class HomePreviewParameterProviders : PreviewParameterProvider<HomeViewModel.UIState> {

    override val values: Sequence<HomeViewModel.UIState>
        get() = sequenceOf(
            HomeViewModel.UIState(
                ringtones = listOf(
                    RingtoneBO(
                        "1",
                        "Bella Ciao",
                        artist = "Maneskin",
                        source = "La Casa de Papel",
                        fileUrl = "https://www.example.com/bellaciao.mp3"
                    ),
                    RingtoneBO(
                        "2",
                        "Quiero ser",
                        artist = "Mago de Oz",
                        source = "La Casa de Papel",
                        fileUrl = "https://www.example.com/quieroser.mp3"
                    ),
                    RingtoneBO(
                        "3",
                        "Sintiéndolo Mucho",
                        artist = "Los Secretos",
                        source = "Élite",
                        fileUrl = "https://www.example.com/sintiendolomucho.mp3"
                    ),
                    RingtoneBO(
                        "4",
                        "Toca Toca",
                        artist = "Fly Project",
                        source = "La Casa de las Flores",
                        fileUrl = "https://www.example.com/tocatoca.mp3"
                    ),
                    RingtoneBO(
                        "5",
                        "Alma de cantautor",
                        artist = "José Antonio Ramos Sucre",
                        source = "Las Chicas del Cable",
                        fileUrl = "https://www.example.com/almadecantautor.mp3"
                    ),
                    RingtoneBO(
                        "6",
                        "A lo lejos",
                        artist = "La India",
                        source = "Vis a Vis",
                        fileUrl = "https://www.example.com/alolejos.mp3"
                    ),
                    RingtoneBO(
                        "7",
                        "El Regalo de la Diosa",
                        artist = "Soleá Morente",
                        source = "Los Serrano",
                        fileUrl = "https://www.example.com/elregalo.mp3"
                    ),
                    RingtoneBO(
                        "8",
                        "Quédate",
                        artist = "Sebastián Yatra",
                        source = "Élite",
                        fileUrl = "https://www.example.com/quedate.mp3"
                    ),
                    RingtoneBO(
                        "9",
                        "Merlí",
                        artist = "Santi Balmes",
                        source = "Merlí",
                        fileUrl = "https://www.example.com/merli.mp3"
                    ),
                    RingtoneBO(
                        "10",
                        "La vida es un carnaval",
                        artist = "Celia Cruz",
                        source = "La Casa de las Flores",
                        fileUrl = "https://www.example.com/lavidaesuncarnaval.mp3"
                    ),
                    RingtoneBO(
                        "11",
                        "Canción del adiós",
                        artist = "Los Secretos",
                        source = "Cuentame cómo pasó",
                        fileUrl = "https://www.example.com/canciondeladios.mp3"
                    ),
                    RingtoneBO(
                        "12",
                        "Deja que te bese",
                        artist = "Carlos Vives & Shakira",
                        source = "Las Chicas del Cable",
                        fileUrl = "https://www.example.com/dejatekebese.mp3"
                    ),

                    )
            )
        )
}