package com.example.mlkitapp.ui.nav.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mlkitapp.ui.authentication.screens.LoginScreen
import com.example.mlkitapp.ui.authentication.screens.RegistrationScreen
import com.example.mlkitapp.ui.main.MainScreen
import com.example.mlkitapp.ui.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.nav.routes.NAV_MAIN_SCREEN
import com.example.mlkitapp.ui.nav.routes.NAV_SIGNUP

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
            MainScreen(Modifier.testTag("TAG"))
        }
    }
}