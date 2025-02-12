package com.germandebustamante.ringtonemanager.ui.component.common.scaffold

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScaffold(
    topBarTitle: String?,
    modifier: Modifier = Modifier,
    @DrawableRes navigationIconResource: Int? = null,
    navigationIconClick: () -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(bottom = 0.dp, left = 0.dp, right = 0.dp, top = 0.dp),
                title = {
                    topBarTitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    navigationIconResource?.let {
                        IconButton(onClick = navigationIconClick) {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        content(innerPadding)
    }

}