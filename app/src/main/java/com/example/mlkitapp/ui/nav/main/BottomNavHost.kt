package com.example.mlkitapp.ui.nav.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.R
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.data.utils.TopBarTitleUtils
import com.example.mlkitapp.ui.common.AppBottomNavigation
import com.example.mlkitapp.ui.common.TopBar
import com.example.mlkitapp.ui.main.imageprocess.CameraScreen
import com.example.mlkitapp.ui.main.imageprocess.GalleryScreen
import com.example.mlkitapp.ui.main.map.MapActivity
import com.example.mlkitapp.ui.main.texttospeech.TextToSpeechViewModel
import com.example.mlkitapp.ui.nav.bottomnav.navitems.BottomNavItems
import com.example.mlkitapp.ui.nav.navdrawer.NavigationDrawer
import com.example.mlkitapp.ui.nav.navdrawer.navitems.NavDrawerItems
import com.example.mlkitapp.ui.nav.profile.ProfileNavHost
import com.example.mlkitapp.ui.nav.routes.NAV_PROFILE
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun BottomNavHost() {

    val context = LocalContext.current
    val titleRes = TopBarTitleUtils.topBarTitle
    val navController = rememberNavController()

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    val buttonIcon = remember{ mutableStateOf(Icons.Default.Menu) }

    val textToSpeechViewModel: TextToSpeechViewModel = viewModel()

    val inputType = titleRes.value.let {
        when(it) {
            NavDrawerItems.TextField.title -> {
                stringResource(id = NavDrawerItems.TextField.title)
            }
            else -> {
                stringResource(id = NavDrawerItems.Barcode.title)
            }
        }
    }

    Scaffold(
        modifier = Modifier.testTag("MAIN_SCREEN"),
        scaffoldState = scaffoldState,
        drawerContent = {
            NavigationDrawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController
            )
        },
        topBar = {
            TopBar(
                title = titleRes.value,
                buttonIcon = buttonIcon.value,
                onNavigationButtonClicked = {
                    textToSpeechViewModel.textToSpeech(context, "Now you have opened the navigation bar, where you can pick what do you want to analyze, text or barcode. The text is the first option, the barcode is the second option.")
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onProfileButtonClicked = {
                    textToSpeechViewModel.textToSpeech(context, "Now you are on the profile screen, where you can do several actions. By clicking on the first option, you can manage your account. By clicking on the second option you can open your saved posts. By clicking on the third option you can open app's settings. By clicking on the last option you can logout from the application.")
                    navController.navigate(NAV_PROFILE) {
                        SharedPreferences.setCurrentNavRoute(NAV_PROFILE)
                        SharedPreferences.setTargetNavRoute(BottomNavItems.Home.navRoute)
                        popUpTo(BottomNavItems.Home.navRoute) { inclusive = true }
                    }
                },
                scaffold = scaffoldState,
                coroutineScope = scope,
                navigationController = navController
            ) {
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItems.Home.navRoute
                ) {
                    composable(BottomNavItems.Home.navRoute) {
                        TopBarTitleUtils.changeTitle(SharedPreferences.selectedInput.value)
                        buttonIcon.value = Icons.Default.Menu
                        CameraScreen(
                            inputType = stringResource(id = titleRes.value),
                            hiltViewModel()
                        )
                    }
                    composable(BottomNavItems.Gallery.navRoute) {
                        TopBarTitleUtils.changeTitle(SharedPreferences.selectedInput.value)
                        buttonIcon.value = Icons.Default.Menu
                        GalleryScreen(
                            inputType = inputType,
                            hiltViewModel()
                        )

                    }
                    composable(BottomNavItems.Map.navRoute) {
                        buttonIcon.value = Icons.Default.Menu
                        context.startActivity(Intent(context, MapActivity::class.java))
                    }
                    composable(NAV_PROFILE) {
                        TopBarTitleUtils.changeTitle(R.string.profile)
                        buttonIcon.value = Icons.Default.ArrowBack
                        ProfileNavHost(
                            rememberNavController()
                        )
                    }
                }
            }
        },
        bottomBar = {
            AppBottomNavigation(navController = navController, context)
        },
    ) {

    }
}