package com.example.mlkitapp.ui.main.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mlkitapp.data.utils.LocationUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {
    val budapest = LatLng(47.497913, 19.040236)
    val budapestState = rememberMarkerState(position = budapest)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(budapest, 10f)
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }


    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ){
            Marker(
                state = budapestState,
                title = "Budapest"
            )
        }

    }
}

@Composable
private fun GoogleMap(
    currLocation: Location,
    cameraPositionState: CameraPositionState,
    onGpsIconClick: () -> Unit
){
    val mapUiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings
    ) {
        Marker(
            state = MarkerState(
                position = LocationUtils.getPosition(currLocation)
            ),
            title = "Current position"
        )
    }

    GpsIconButton(onIconClick = onGpsIconClick)

    DebugOverlay(cameraPositionState)
}

fun isLocationPermissionGranted(context: Context) : Boolean {
    return (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
}

@Composable
fun LocationPermissionsDialog(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val requestLocationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    SideEffect {
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

@Composable
private fun GpsIconButton(onIconClick: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(
                onClick = onIconClick
            ) {
                Icon(
                    Icons.Default.Warning,
                    modifier = Modifier.padding(bottom = 100.dp, end = 20.dp),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun DebugOverlay(
    cameraPositionState: CameraPositionState,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        val moving =
            if (cameraPositionState.isMoving) "moving" else "not moving"
        Text(
            text = "Camera is $moving",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray)
        Text(
            text = "Camera position is ${cameraPositionState.position}",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray)
    }
}


@Preview
@Composable
fun MapScreenPreview(){
    MapScreen()
}