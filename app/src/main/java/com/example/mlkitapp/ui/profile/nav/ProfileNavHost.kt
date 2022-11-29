package com.example.mlkitapp.ui.profile.nav

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mlkitapp.R
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.data.utils.TopBarTitleUtils
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.authentication.FirebaseActivity
import com.example.mlkitapp.ui.main.nav.routes.NAV_CLICKED_ITEM
import com.example.mlkitapp.ui.main.nav.routes.NAV_CLICKED_ITEM_OPEN_MAP
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGOUT
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE_ACCOUNT_MANAGEMENT
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE_SETTINGS
import com.example.mlkitapp.ui.main.nav.routes.NAV_SAVED
import com.example.mlkitapp.ui.main.screens.TextToSpeechViewModel
import com.example.mlkitapp.ui.profile.ProfileNavItemsUi
import com.example.mlkitapp.ui.profile.screens.AccountManagerScreen
import com.example.mlkitapp.ui.profile.screens.ClickedItemMapScreen
import com.example.mlkitapp.ui.profile.screens.SavedScreen
import com.example.mlkitapp.ui.profile.screens.SettingsScreen
import com.example.mlkitapp.ui.profile.screens.TextInfoScreen


@OptIn(InternalComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileNavHost(
    navController: NavHostController,
    textToSpeechViewModel: TextToSpeechViewModel = viewModel(),
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NAV_PROFILE
    ) {
        composable(NAV_PROFILE) {
            ProfileNavItemsUi(
                navController = navController,
                onDestinationClicked = { route ->
                    if (route == NAV_LOGOUT) {
                        viewModel.logout()
                        context.startActivity(Intent(context, FirebaseActivity::class.java))
                    } else {
                        textToSpeechViewModel.textToSpeech(context, route)
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                })
        }
        composable(NAV_SAVED){
            TopBarTitleUtils.changeTitle(R.string.saved)
            SharedPreferences.setCurrentNavRoute(NAV_SAVED)
            SharedPreferences.setTargetNavRoute(NAV_PROFILE)
            SavedScreen(
                hiltViewModel(),
                navController
            )
        }
        composable(NAV_PROFILE_ACCOUNT_MANAGEMENT) {
            TopBarTitleUtils.changeTitle(R.string.account)
            SharedPreferences.setCurrentNavRoute(NAV_PROFILE_ACCOUNT_MANAGEMENT)
            SharedPreferences.setTargetNavRoute(NAV_PROFILE)
            AccountManagerScreen(
                viewModel,
            )
        }
        composable(NAV_PROFILE_SETTINGS) {
            TopBarTitleUtils.changeTitle(R.string.settings)
            SharedPreferences.setCurrentNavRoute(NAV_PROFILE_SETTINGS)
            SharedPreferences.setTargetNavRoute(NAV_PROFILE)
            SettingsScreen(
                navController,
            )
        }
        composable(NAV_CLICKED_ITEM){
            TopBarTitleUtils.changeTitle(R.string.empty)
            SharedPreferences.setCurrentNavRoute(NAV_CLICKED_ITEM)
            SharedPreferences.setTargetNavRoute(NAV_SAVED)
            TextInfoScreen(
                navController,
            )
        }
        composable(NAV_CLICKED_ITEM_OPEN_MAP){
            TopBarTitleUtils.changeTitle(R.string.empty)
            SharedPreferences.setCurrentNavRoute(NAV_CLICKED_ITEM_OPEN_MAP)
            SharedPreferences.setTargetNavRoute(NAV_CLICKED_ITEM)
            ClickedItemMapScreen()
        }
    }
}