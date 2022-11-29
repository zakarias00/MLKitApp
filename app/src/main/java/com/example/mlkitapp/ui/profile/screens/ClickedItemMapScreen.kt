package com.example.mlkitapp.ui.profile.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mlkitapp.R
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.ui.main.screens.bitmapDescriptorFromVector
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClickedItemMapScreen() {
    val clickedItem = SharedPreferences.sharedRecognizedText!!

    val clickedItemPos = LatLng(clickedItem.latitude!!, clickedItem.longitude!!)
    val clickedItemState = rememberMarkerState(position = clickedItemPos)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(clickedItemPos, 12f)
    }

    val mapIcon = bitmapDescriptorFromVector(LocalContext.current, R.drawable.ic_locations_48dp)

    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val properties by remember { mutableStateOf(MapProperties()) }

    Box(
        Modifier.fillMaxSize()
            .padding(bottom = 56.dp)
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = clickedItemState,
                title = clickedItem.address,
                icon = mapIcon
            )
        }
    }
}