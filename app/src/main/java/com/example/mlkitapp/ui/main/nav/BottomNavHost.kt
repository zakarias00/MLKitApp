package com.example.mlkitapp.ui.main.nav

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.R
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.data.utils.TopBarTitleUtils
import com.example.mlkitapp.ui.common.AppBottomNavigation
import com.example.mlkitapp.ui.common.NavigationDrawer
import com.example.mlkitapp.ui.common.TopBar
import com.example.mlkitapp.ui.main.MapActivity
import com.example.mlkitapp.ui.main.nav.navitems.BottomNavItems
import com.example.mlkitapp.ui.main.nav.navitems.NavDrawerItems
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE
import com.example.mlkitapp.ui.main.screens.GalleryScreen
import com.example.mlkitapp.ui.main.screens.MainContent
import com.example.mlkitapp.ui.main.screens.TextToSpeechViewModel
import com.example.mlkitapp.ui.profile.nav.ProfileNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
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

    Scaffold(
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
                onBackClicked = {
                    TopBarTitleUtils.changeTitle(NavDrawerItems.TextField.title)
                    navController.navigateUp()
                },
                onNavigationButtonClicked = {
                    textToSpeechViewModel.textToSpeech(context, "Now you have opened the navigation bar, where you can pick what do you want to analyze, text or barcode. The text is the first option, the barcode is the second option.")
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onProfileButtonClicked = {
                    textToSpeechViewModel.textToSpeech(context, "Now you are on the profile screen, where you can do several actions. By clicking on the first option, you can manage your account.")
                    navController.navigate(NAV_PROFILE) {
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
                        MainContent(Modifier.fillMaxSize())
                       // ImagePicker(
//                            inputType = stringResource(id = titleRes.value),
//                            hiltViewModel(),
//                            hiltViewModel()
//                        )
                    }
                    composable(BottomNavItems.Gallery.navRoute) {
                        TopBarTitleUtils.changeTitle(SharedPreferences.selectedInput.value)
                        buttonIcon.value = Icons.Default.Menu
                        if(titleRes.value == NavDrawerItems.TextField.title) {
                            GalleryScreen(
                                inputType = stringResource(id = NavDrawerItems.TextField.title),
                                hiltViewModel(),
                                hiltViewModel()
                            )
                        }
                        else if(titleRes.value == NavDrawerItems.Barcode.title){
                            GalleryScreen(
                                inputType = stringResource(id = NavDrawerItems.Barcode.title),
                                hiltViewModel(),
                                hiltViewModel()
                            )
                        }
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


fun NavHostController.navigateBack(
    targetRoute: String,
    currentRoute: String
) {
    val previousRoute = previousBackStackEntry?.destination?.route ?: "null"

    if (previousRoute == targetRoute) popBackStack()
    else navigate(route = targetRoute) {
        popUpTo(route = currentRoute) { inclusive = true }
        launchSingleTop = true
    }
}
