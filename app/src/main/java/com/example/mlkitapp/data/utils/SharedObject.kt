package com.example.mlkitapp.data.utils

import androidx.compose.runtime.mutableStateOf
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.ui.main.nav.navitems.NavDrawerItems
import com.google.android.gms.maps.model.LatLng

object SharedObject {
    var sharedRecognizedText: RecognizedText? = null

    var currentLocation = mutableStateOf(LocationUtils.getDefaultLocation())
    var updateLocation = mutableStateOf(true)
    var selectedInput = mutableStateOf(NavDrawerItems.TextField.title)

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
}