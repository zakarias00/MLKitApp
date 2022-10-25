package com.example.mlkitapp.ui.main.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.common.SaveTextDialog
import com.example.mlkitapp.ui.main.barcodescan.BarcodeScannerViewModel
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel
import com.google.mlkit.vision.common.InputImage

@OptIn(InternalComposeApi::class)
@Composable
fun GalleryScreen(
    textRecognViewModel: TextRecognViewModel,
    barcodeScannerViewModel: BarcodeScannerViewModel
) {
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val textVal = remember {
        mutableStateOf(" ")
    }
    val recognizedTextFlow = textRecognViewModel.recognizedTextFlow.collectAsState()
    val barcodeScannerFlow = barcodeScannerViewModel.barcodeFlow.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Image(
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .crossfade(false)
                    .data(imageUri)
                    .build(),
                    filterQuality = FilterQuality.High
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .wrapContentWidth()
                .size(420.dp)
                .padding(5.dp)
        )

        Row(
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Choose photo")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {
                    if(imageUri != null){
                        textRecognViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri!!))
                    }
                    else{
                        Toast.makeText(context, "You have to choose an image first!", Toast.LENGTH_SHORT).show()
                    }
                })
            {
                Text(text = "Text")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {
                if(imageUri != null){
                    barcodeScannerViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri!!))
                }
                else{
                    Toast.makeText(context, "You have to choose an image first!", Toast.LENGTH_SHORT).show()
                }
            })
            {
                Text(text = "Barcode")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                onClick = { showDialog = !showDialog },
                modifier = Modifier.wrapContentSize(),
                enabled = textVal.value.isNotEmpty()
            )
            {
                Text(text = "Save")
            }
        }
        
        Text(
            text = textVal.value
        )

        recognizedTextFlow.value.let {
            when(it){
                is Resource.Success -> {
                    textVal.value = it.result.text
                }
                is Resource.Failure -> {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(10.dp)
                    )
                }
            }
        }

        barcodeScannerFlow.value.let {
            when(it){
                is Resource.Success -> {
                    textVal.value = it.result
                }
                is Resource.Failure -> {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(10.dp)
                    )
                }
            }
        }
        
        if(showDialog){
            SaveTextDialog(onDismiss = { showDialog = !showDialog }, hiltViewModel(), textVal.value, imageUri.toString())
        }

    }
}

@Preview
@Composable
fun GalleryScreenPreview() {
    //GalleryScreen()
}
