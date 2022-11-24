package com.example.mlkitapp.ui.main.screens

import android.annotation.SuppressLint
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.data.utils.LocationUtils
import com.example.mlkitapp.data.utils.LocationUtils.fusedLocationProviderClient
import com.example.mlkitapp.ui.main.db.CloudDbViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(InternalComposeApi::class)
@SuppressLint("MissingPermission")
@Composable
fun MaScreen(
    dbViewModel: CloudDbViewModel
) {
    val context = LocalContext.current

    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LocationUtils.getPosition(currentLocation), 12f)

    var requestLocationUpdate by remember { mutableStateOf(true) }

    val textsFlow = dbViewModel.getDocumentsFlow.value
    var textList: List<RecognizedText> = mutableListOf()

    val mapUiSettings by remember { mutableStateOf(MapUiSettings()) }

    var showDialog by remember { mutableStateOf(false) }

    if (requestLocationUpdate) {
        LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->
            locationResult.lastLocation?.let { location ->
                currentLocation = location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LocationUtils.getPosition(location), 12f)
            }
        }
        requestLocationUpdate = false
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = mapUiSettings
    ) {
        textList.forEach { item ->
            if (!item.private!! && isNearBy(currentLocation, LatLng(item.latitude!!, item.longitude!!))) {
                MarkerInfoWindow(
                    state = MarkerState(
                        position = LatLng(
                            item.latitude,
                            item.longitude
                        )
                    ),
                    //snippet =
//                    onInfoWindowClick = {
//                        showDialog = !showDialog
//                    },
                    //title = item.title
                ){
                    Card(
                        modifier = Modifier.wrapContentSize(),
                        shape = RoundedCornerShape(25.dp),
                        border = BorderStroke(4.dp, MaterialTheme.colors.primary)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .wrapContentSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                item.title.toString()
                            )

                            Text(
                                item.recognizedText.toString()
                            )

                            if (item.imageUri != null) {
                                Image(
                                    rememberAsyncImagePainter(model = ImageRequest.Builder(context)
                                        .crossfade(false)
                                        .data(item.imageUri)
                                        .build(),
                                        filterQuality = FilterQuality.High),
                                    contentDescription = "Taken image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(16.dp)
                                )
                            }
                            }
                        }
                    }
                }


//                MarkerInfoWindowContent(
//                    state = MarkerState(
//                        position = LatLng(
//                            item.latitude,
//                            item.longitude
//                        )
//                    ),
//                    onInfoWindowClick = {
//                        showDialog = !showDialog
//                    },
//                    title = item.title
//                )
            }
        }



    textsFlow.let { texts ->
        when (texts) {
            is Resource.Success -> {
                textList = texts.result.toMutableList()
            }
            is Resource.Failure -> {
                Toast.makeText(LocalContext.current, texts.exception.message, Toast.LENGTH_LONG).show()
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(10.dp)
                )
            }
        }
    }

//    if(showDialog) {
//        MarkerInfoWindow(
//            title = "szia"
//        )
//    }
}

private fun isNearBy(current: Location, cloud: LatLng): Boolean{
    val cloudLocation = Location("")
    cloudLocation.longitude = cloud.longitude
    cloudLocation.latitude = cloud.latitude

    val distance = current.distanceTo(cloudLocation)
    return distance < 2000.0
}
