package com.example.mlkitapp.ui.main.nav

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.ui.common.AppBottomNavigation
import com.example.mlkitapp.ui.common.TopBar
import com.example.mlkitapp.ui.main.MainActivity
import com.example.mlkitapp.ui.main.nav.navitems.BottomNavItems
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE
import com.example.mlkitapp.ui.main.screens.BuildCameraUi
import com.example.mlkitapp.ui.main.screens.GalleryScreen
import com.example.mlkitapp.ui.main.screens.MapScreen
import com.example.mlkitapp.ui.main.screens.SavedScreen
import kotlinx.coroutines.launch

@OptIn(InternalComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavHost() {

    val context = LocalContext.current
    val title = remember { mutableStateOf(BottomNavItems.Home.title) }
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()
    //val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                title = title.value,
                onNavigationButtonClicked = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onProfileButtonClicked = {
                    navController.navigate(NAV_PROFILE) {
                        popUpTo(BottomNavItems.Home.navRoute) { inclusive = true }
                    }
                },
                content = {
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItems.Home.navRoute
                    ) {
                        composable(BottomNavItems.Home.navRoute) {
                            BuildCameraUi(
                                context = context,
                                textRecognViewModel = hiltViewModel(),
                            )
                        }
                        composable(BottomNavItems.Gallery.navRoute) {
                            GalleryScreen(
                                hiltViewModel(), hiltViewModel()
                            )
                        }
                        composable(BottomNavItems.Saved.navRoute) {
                            SavedScreen(hiltViewModel())
                        }
                        composable(BottomNavItems.Map.navRoute) {
                            MapScreen()
                        }
                        composable(NAV_PROFILE){
                            LaunchedEffect(Unit) {
                                context.startActivity(Intent(context, MainActivity::class.java))
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            AppBottomNavigation(navController = navController)
        },
    ) {

    }
}