package com.example.mlkitapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
inline fun <reified VM : ViewModel> NavController.getViewModelInstance(navBackStackEntry: NavBackStackEntry, route: String): VM {
    val parentEntry = getParent(navBackStackEntry, route)
    return hiltViewModel(parentEntry)
}

@Composable
fun NavController.getParent(navBackStackEntry: NavBackStackEntry, route: String): NavBackStackEntry {
    val parentEntry = remember(navBackStackEntry) {
        this.getBackStackEntry(route)
    }
    return parentEntry
}