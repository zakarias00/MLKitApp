package com.example.mlkitapp.ui.common

import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mlkitapp.ui.main.nav.navitems.BottomNavItems
import com.example.mlkitapp.ui.main.screens.TextToSpeechViewModel

@Composable
fun AppBottomNavigation(
    navController: NavController,
    context: Context
) {

    val bottomNavItems = listOf(BottomNavItems.Home, BottomNavItems.Gallery, BottomNavItems.Map)

    val textToSpeechViewModel: TextToSpeechViewModel = viewModel()

    BottomNavigation(
        modifier = Modifier.height(72.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 26.dp,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = stringResource(item.title), tint = Color.White, modifier = Modifier.padding(bottom = 8.dp))},
                label = { Text(text = stringResource(item.title), fontSize = 16.sp, )},
                selectedContentColor = MaterialTheme.colors.background,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = false,
                selected = currentRoute == item.navRoute,
                onClick = {
                    textToSpeechViewModel.textToSpeech(context, "Now you are on ${item.navRoute} screen")
                    navController.navigate(item.navRoute) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }

    }
}
