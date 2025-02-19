package com.germandebustamante.ringtonemanager.ui.component.common.button.google

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.germandebustamante.ringtonemanager.R
import com.germandebustamante.ringtonemanager.ui.component.common.button.ButtonSize
import com.germandebustamante.ringtonemanager.ui.session.AccountManager
import com.germandebustamante.ringtonemanager.ui.theme.RingtoneManagerTheme
import kotlinx.coroutines.launch

@Composable
fun ButtonGoogleSignIn(
    onGoogleIdTokenReceived: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val accountManager = remember {
        AccountManager(context)
    }

    Button(
        onClick = {
            scope.launch {
                accountManager.googleSignIn(onGoogleIdTokenReceived)
            }
        },
        modifier = modifier
            .defaultMinSize(minWidth = ButtonSize.LARGE.minWidth.dp)
            .heightIn(min = ButtonSize.LARGE.height.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Access using Google",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode")
@Composable
fun ButtonGoogleSignInPreview() {
    RingtoneManagerTheme {
        ButtonGoogleSignIn(
            onGoogleIdTokenReceived = {}
        )
    }
}