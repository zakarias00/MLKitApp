package com.example.mlkitapp.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.InternalComposeApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mlkitapp.data.utils.LocationUtils.fusedLocationProviderClient
import com.example.mlkitapp.ui.BaseActivity
import com.example.mlkitapp.ui.main.screens.MapScreen
import com.example.mlkitapp.ui.theme.MLKitAppTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity: BaseActivity() {

    @OptIn(InternalComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(applicationContext)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            MLKitAppTheme {
                MapScreen(
                    hiltViewModel()
                )
            }
        }
    }
}