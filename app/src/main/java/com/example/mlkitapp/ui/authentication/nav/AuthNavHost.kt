package com.example.mlkitapp.ui.authentication.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mlkitapp.ui.authentication.screens.LoginScreen
import com.example.mlkitapp.ui.authentication.screens.RegistrationScreen
import com.example.mlkitapp.ui.main.MainActivityScreen
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.main.nav.routes.NAV_MAIN_SCREEN
import com.example.mlkitapp.ui.main.nav.routes.NAV_SIGNUP

@Composable
fun AuthNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NAV_LOGIN
    ) {
        composable(NAV_LOGIN) {
            LoginScreen(hiltViewModel(), navController)
        }
        composable(NAV_SIGNUP) {
            RegistrationScreen(hiltViewModel(), navController)
        }
        composable(NAV_MAIN_SCREEN){
            MainActivityScreen()
        }
    }
}