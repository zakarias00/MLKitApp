package com.example.mlkitapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.data.utils.LocationUtils.fusedLocationProviderClient
import com.example.mlkitapp.ui.nav.auth.AuthNavHost
import com.example.mlkitapp.ui.theme.MLKitAppTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirebaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MapsInitializer.initialize(applicationContext)
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            MLKitAppTheme {
                AuthNavHost(rememberNavController())
            }
        }
    }
}