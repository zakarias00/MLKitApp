package com.example.mlkitapp.ui.common

import androidx.compose.foundation.layout.absolutePadding
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mlkitapp.ui.main.nav.navitems.BottomNavItems

@Composable
fun AppBottomNavigation(navController: NavController) {

    val bottomNavItems = listOf(BottomNavItems.Home, BottomNavItems.Gallery, BottomNavItems.Saved, BottomNavItems.Map)

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 40.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = stringResource(item.title), tint = Color.Black, modifier = Modifier.absolutePadding(top = 6.dp))},
                label = { Text(text = stringResource(item.title), fontSize = 12.sp, modifier = Modifier.absolutePadding(top = 6.dp))},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.navRoute,
                onClick = {
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
