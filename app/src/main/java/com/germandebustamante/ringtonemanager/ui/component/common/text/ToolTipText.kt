package com.germandebustamante.ringtonemanager.ui.component.common.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme

@Composable
fun ToolTipText(text: String, modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (tooltipPolygon, tooltipText) = createRefs()

        Text(
            text = text,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.inversePrimary)
                .padding(6.dp)
                .constrainAs(tooltipText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            style = MaterialTheme.typography.bodySmall,
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.tooltip_polygion_ic),
            contentDescription = null,
            modifier = Modifier.constrainAs(tooltipPolygon) {
                start.linkTo(tooltipText.start, margin = 18.dp)
                top.linkTo(tooltipText.bottom, margin = (-6).dp)
            },
            tint = MaterialTheme.colorScheme.inversePrimary
        )
    }
}

@Preview
@Composable
fun ToolTipTextPreview() {
    RingtoneManagerTheme {
        ToolTipText(text = "Share ringtone with your friends")
    }
}