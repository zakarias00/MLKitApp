package com.example.mlkitapp.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

@Composable
fun TopBar(
    @StringRes title: Int,
    buttonIcon: ImageVector,
    onBackClicked: () -> Unit,
    onNavigationButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit,
    scaffold: ScaffoldState,
    coroutineScope: CoroutineScope,
    navigationController: NavController,
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
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                if (buttonIcon == Icons.Filled.Menu) {
                    IconButton(
                        onClick = {
                            onNavigationButtonClicked()
                        },
                    )
                    {
                        Icon(
                            imageVector = buttonIcon,
                            contentDescription = "Menu icon",
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                } else if (buttonIcon == Icons.Default.ArrowBack) {
                    IconButton(
                        onClick = {
                            onBackClicked()
                        }
                    ) {
                        Icon(
                            imageVector = buttonIcon,
                            contentDescription = "Arrow-back icon",
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = { onProfileButtonClicked() }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Profile icon",
                        tint = MaterialTheme.colors.primaryVariant
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )
        content.invoke()
    }
}
