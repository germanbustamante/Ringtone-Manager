package com.germandebustamante.ringtonemanager.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onRingtoneClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    HomeScreen(
        state = viewModel.state,
        onRingtoneClicked = onRingtoneClicked,
        modifier = modifier
    )
}

@Composable
private fun HomeScreen(
    state: HomeViewModel.UIState,
    onRingtoneClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(state.ringtones) { ringtone ->
            Text(text = ringtone.name)

            Button(onClick = { onRingtoneClicked(ringtone.id) }) {
                Text("Go to detail")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomePreviewParameterProviders::class) state: HomeViewModel.UIState,
) {
    HomeScreen(state, onRingtoneClicked = {})
}