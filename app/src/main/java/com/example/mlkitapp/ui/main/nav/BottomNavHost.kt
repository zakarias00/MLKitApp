package com.example.mlkitapp.ui.main.nav

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.ui.main.screens.SavedScreen
import com.example.mlkitapp.ui.common.TopBar
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.authentication.FirebaseActivity
import com.example.mlkitapp.ui.main.screens.GalleryScreen
import com.example.mlkitapp.ui.main.screens.MapScreen
import com.example.mlkitapp.ui.main.nav.navitems.BottomNavItems
import com.example.mlkitapp.ui.common.AppBottomNavigation
import com.example.mlkitapp.ui.main.textrecognition.BuildCameraUi
import kotlinx.coroutines.launch

@OptIn(InternalComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BottomNavHost() {

    val context = LocalContext.current
    val title = remember { mutableStateOf(BottomNavItems.Home.title) }
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()
    val viewModel: AuthViewModel = hiltViewModel()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    Scaffold(
        topBar = {
            TopBar(
                title = title.value,
                onNavigationButtonClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onProfileButtonClicked = {
                    viewModel.logout()
                    context.startActivity(Intent(context, FirebaseActivity::class.java))
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
//                            Log.i("LOC", LocationUtils.getDefaultLocation().toString())
//                            AppScreen(text = LocationUtils.getDefaultLocation().toString())
                        }
                        composable(BottomNavItems.Map.navRoute) {
                            MapScreen()
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
//private fun onOpenSettingsClick() {
//    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//    val uri: Uri = Uri.fromParts("package", packageName, null)
//    intent.data = uri
//    startActivity(intent)
//}