package com.germandebustamante.ringtonemanager.ui.screen.ringtone

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun RingtoneDetailScreen(modifier: Modifier = Modifier, viewModel: RingtoneDetailViewModel = koinViewModel()) {
    Text("Ringtone Detail Screen with id ${viewModel.ringtoneId}")
}