package com.example.mlkitapp.ui.main.map.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mlkitapp.data.utils.LocationUtils
import com.google.android.gms.maps.model.LatLng

class LocationViewModel: ViewModel() {

    var currentLocation = mutableStateOf(LocationUtils.getDefaultLocation())
    var updateLocation = mutableStateOf(true)

    fun getCurrLocation(): LatLng {
        if(updateLocation.value){
            LocationUtils.requestLocationResultCallback(LocationUtils.fusedLocationProviderClient) { locationResult ->
                locationResult.lastLocation?.let { location ->
                    currentLocation.value = location
                }
            }
        }
        return LatLng(currentLocation.value.latitude, currentLocation.value.longitude)
    }

}