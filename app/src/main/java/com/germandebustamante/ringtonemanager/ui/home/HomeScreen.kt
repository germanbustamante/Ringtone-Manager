package com.germandebustamante.ringtonemanager.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = koinViewModel()) {

    val state = viewModel.state

    LazyColumn(modifier = modifier) {
        items(state.ringtones) { ringtone ->
            Text(text = ringtone.name)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}