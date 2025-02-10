package com.germandebustamante.ringtonemanager.ui.component.common.ringtone

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.germandebustamante.ringtonemanager.R

@Composable
fun RandomRingtoneBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = randomBackgrounds.random()),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Preview
@Composable
private fun RandomRingtoneBackgroundPreview() {
    RandomRingtoneBackground()
}

private val randomBackgrounds = listOf(
    R.mipmap.ic_random_ringtone_bg_1,
    R.mipmap.ic_random_ringtone_bg_2,
    R.mipmap.ic_random_ringtone_bg_3,
    R.mipmap.ic_random_ringtone_bg_4,
    R.mipmap.ic_random_ringtone_bg_5,
    R.mipmap.ic_random_ringtone_bg_6,
    R.mipmap.ic_random_ringtone_bg_7,
    R.mipmap.ic_random_ringtone_bg_8,
    R.mipmap.ic_random_ringtone_bg_9,
)