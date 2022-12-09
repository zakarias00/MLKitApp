package com.example.mlkitapp.ui.main.map.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mlkitapp.R
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.data.utils.LocationUtils
import com.example.mlkitapp.data.utils.LocationUtils.fusedLocationProviderClient
import com.example.mlkitapp.data.utils.Resource
import com.example.mlkitapp.ui.FirebaseActivity
import com.example.mlkitapp.ui.main.saved.viewmodel.CloudDbViewModel
import com.example.mlkitapp.ui.main.texttospeech.TextToSpeechViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
fun MapScreen(
    dbViewModel: CloudDbViewModel
){
    val context = LocalContext.current

    val textToSpeechViewModel: TextToSpeechViewModel = viewModel()

    BackHandler {
        context.startActivity(Intent(context, FirebaseActivity::class.java))
    }
    var currentLocation by remember { mutableStateOf(LocationUtils.getDefaultLocation()) }

    val cameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(
        LocationUtils.getPosition(currentLocation), 12f)

    var requestLocationUpdate by remember { mutableStateOf(true) }

    val textsFlow = dbViewModel.getDocumentsFlow.value
    var textList: List<RecognizedText> = mutableListOf()

    val mapUiSettings by remember { mutableStateOf(MapUiSettings()) }

    if (requestLocationUpdate) {
        LocationUtils.requestLocationResultCallback(fusedLocationProviderClient) { locationResult ->
            locationResult.lastLocation?.let { location ->
                currentLocation = location
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LocationUtils.getPosition(location), 18f)
            }
        }
        requestLocationUpdate = false
    }

    val isSelected = remember{ mutableStateOf(false) }

    val icon = remember {
        mutableStateOf(if (!isSelected.value) bitmapDescriptorFromVector(context, R.drawable.ic_locations_32dp) else bitmapDescriptorFromVector(context, R.drawable.ic_location_selected))
    }

    textsFlow.let { texts ->
        when (texts) {
            is Resource.Success -> {
                textList = texts.result.toMutableList().filter {
                    it.private == false && isNearBy(currentLocation, LatLng(it.latitude!!, it.longitude!!))
                }
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

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = mapUiSettings
    ) {
        textList.forEach { item ->
            isSelected.value = false
            MarkerInfoWindow(
                 state = MarkerState(
                     position = LatLng(
                        item.latitude!!,
                        item.longitude!!
                     )
                 ),
                 icon = icon.value,
             )
            {
                 val textToRead = "Post's information: location is ${item.address.toString()}" +
                         "Recognized text is ${item.recognizedText}"
                 textToSpeechViewModel.textToSpeech(context, textToRead)

                 Box(
                     modifier = Modifier
                         .padding(32.dp)
                         .background(
                             color = MaterialTheme.colors.onPrimary,
                             shape = RoundedCornerShape(35.dp)
                         )
                     ,
                 ) {
                     Column(
                         modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                         horizontalAlignment = Alignment.CenterHorizontally
                     ) {

                         if (item.imageUri != null) {
                             Image(
                                 rememberAsyncImagePainter(
                                     model = ImageRequest.Builder(LocalContext.current)
                                         .data(item.imageUri)
                                         .crossfade(false)
                                         .allowHardware(false)
                                         .build(),
                                     filterQuality = FilterQuality.High),
                                 contentDescription = null,
                                 modifier = Modifier
                                     .height(80.dp)
                                     .fillMaxWidth(),
                             )
                         }

                         Spacer(modifier = Modifier.height(16.dp))

                         Text(
                             text = item.address.toString(),
                             textAlign = TextAlign.Center,
                             modifier = Modifier
                                 .fillMaxWidth(),
                             style = MaterialTheme.typography.h6,
                             color = MaterialTheme.colors.primary,
                         )
                         Spacer(modifier = Modifier.height(8.dp))

                         Text(
                             text = item.recognizedText.toString(),
                             textAlign = TextAlign.Center,
                             modifier = Modifier
                                 .padding(horizontal = 25.dp)
                                 .fillMaxWidth(),
                             style = MaterialTheme.typography.body1,
                             color = MaterialTheme.colors.primary,
                         )
                         Spacer(modifier = Modifier.height(8.dp))
                     }
                 }
             }
        }
    }
}

private fun isNearBy(current: Location, cloud: LatLng): Boolean{
    val cloudLocation = Location("")
    cloudLocation.longitude = cloud.longitude
    cloudLocation.latitude = cloud.latitude

    val distance = current.distanceTo(cloudLocation)
    return distance < 2000.0
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(10, 10, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}