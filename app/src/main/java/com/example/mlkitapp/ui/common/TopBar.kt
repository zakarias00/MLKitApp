package com.example.mlkitapp.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TopBar(
@StringRes title: Int,
buttonIcon: ImageVector = Icons.Filled.Menu,
onNavigationButtonClicked: () -> Unit,
onProfileButtonClicked: () -> Unit,
content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { onNavigationButtonClicked() }) {
                    Icon(
                        imageVector = buttonIcon,
                        contentDescription = "icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            },
            actions = {
                      IconButton(onClick = { onProfileButtonClicked() }) {
                          Icon(
                              imageVector = Icons.Rounded.Person,
                              contentDescription = "icon",
                              tint = MaterialTheme.colors.onPrimary
                          )
                      }
            },
            backgroundColor = MaterialTheme.colors.primary
        )
        content.invoke()
    }
}
