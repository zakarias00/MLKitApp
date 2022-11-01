package com.example.mlkitapp.ui.profile.nav

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE_ACCOUNT_MANAGEMENT
import com.example.mlkitapp.ui.main.nav.routes.NAV_PROFILE_SETTINGS
import com.example.mlkitapp.ui.profile.screens.ProfileScreen


@Composable
fun ProfileNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NAV_PROFILE
    ) {
        composable(NAV_PROFILE) {
            ProfileScreen()
        }
        composable(NAV_PROFILE_ACCOUNT_MANAGEMENT) {

        }
        composable(NAV_PROFILE_SETTINGS) {

        }
    }
}

private fun onOpenSettingsClick() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    //val uri: Uri = Uri.fromParts("package", packageName, null)
    //intent.data = uri
    //startActivity(intent)
}