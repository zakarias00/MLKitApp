package com.example.mlkitapp.data.utils

import androidx.compose.runtime.mutableStateOf
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.ui.nav.navdrawer.navitems.NavDrawerItems
import com.example.mlkitapp.ui.nav.routes.NAV_HOME
import com.google.android.gms.maps.model.LatLng

object SharedPreferences {

    var sharedRecognizedText: RecognizedText? = null

    var currentLocation = mutableStateOf(LocationUtils.getDefaultLocation())
    var updateLocation = mutableStateOf(true)

    var selectedInput = mutableStateOf(NavDrawerItems.TextField.title)
    var currentNavRoute = mutableStateOf(NAV_HOME)
    var targetNavRoute = mutableStateOf(NAV_HOME)

    fun getCurrLocation(): LatLng{
        if(updateLocation.value){
            LocationUtils.requestLocationResultCallback(LocationUtils.fusedLocationProviderClient) { locationResult ->
                locationResult.lastLocation?.let { location ->
                    currentLocation.value = location
                }
            }
        }
        return LatLng(currentLocation.value.latitude, currentLocation.value.longitude)
    }

    fun addSharedRecognizedText(recognizedText: RecognizedText){
        sharedRecognizedText = recognizedText
    }

    fun changeInput(inputRes: Int){
        selectedInput.value = inputRes
    }

    fun setTargetNavRoute(navRoute: String){
        targetNavRoute.value = navRoute
    }

    fun setCurrentNavRoute(navRoute: String){
        targetNavRoute.value = navRoute
    }
}