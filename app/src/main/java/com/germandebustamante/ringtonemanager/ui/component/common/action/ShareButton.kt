package com.germandebustamante.ringtonemanager.ui.component.common.action

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.KeyframesSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.ToolTipText
import com.germandebustamante.ringtonemanager.ui.component.common.rigtone.IconPrimaryButtonSize
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import kotlinx.coroutines.delay

private const val ANIMATION_DURATION = 800

@Composable
fun ShareButtonWithToolTip(onClick: () -> Unit, descriptionText: String, modifier: Modifier = Modifier) {
    var showTooltipText by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = Unit) {
        delay(1000) // Delay before showing the tooltip
        showTooltipText = true
        delay(5000) // Tooltip visibility duration
        showTooltipText = false
    }

    ConstraintLayout(
        modifier = modifier,
    ) {
        val (toolTipText, shareButton) = createRefs()

        ShareButton(
            onClick = onClick,
            modifier = Modifier.constrainAs(shareButton) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        AnimatedVisibility(
            visible = showTooltipText,
            enter = fadeIn(animationSpec = getAnimationSpec()),
            exit = fadeOut(animationSpec = getAnimationSpec()),
            modifier = Modifier.constrainAs(toolTipText) {
                bottom.linkTo(shareButton.top)
                start.linkTo(shareButton.start)
            }
        ) {
            ToolTipText(descriptionText)
        }
    }
}

@Composable
private fun ShareButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IconPrimaryButtonSize = IconPrimaryButtonSize.MEDIUM,
) {
    FilledTonalIconButton (
        onClick = onClick,
        modifier = modifier.size(size.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
        ),
        content = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_share_ringtone),
                contentDescription = null,
            )
        }
    )
}

private fun getAnimationSpec(): KeyframesSpec<Float> = keyframes {
    this.durationMillis = ANIMATION_DURATION
}

@Preview
@Composable
fun ShareButtonWithToolTipPreview() {
    RingtoneManagerTheme {
        ShareButtonWithToolTip(onClick = {}, descriptionText = "Share ringtone with your friends!")
    }
}
