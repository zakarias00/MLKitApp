package com.example.mlkitapp.ui.main.imageprocess

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.ui.main.saved.component.SaveTextDialog
import com.example.mlkitapp.ui.main.texttospeech.TextToSpeechViewModel
import com.google.mlkit.vision.common.InputImage

@Composable
fun GalleryScreen(
    inputType: String,
    imageProcessViewModel: ImageProcessViewModel,
    textToSpeechViewModel: TextToSpeechViewModel = viewModel(),
) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
    }

    var textVal = remember { mutableStateOf("") }
    val verticalScroll = rememberScrollState(0)

    val textRecognizerFlow = imageProcessViewModel.recognizedTextFlow.collectAsState()
    val barcodeScannerFlow = imageProcessViewModel.barcodeFlow.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var alreadyExecuted by remember { mutableStateOf(false) }

    if (imageUri != null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            imageUri = null
                            alreadyExecuted = false
                            textVal.value = ""
                            launcher.launch("image/*")
                        },
                    )
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally
        ){
            Image(
                rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .crossfade(false)
                        .data(imageUri)
                        .build(),
                    filterQuality = FilterQuality.High,
                    contentScale = ContentScale.Fit
                ),
                contentDescription = "Chosen image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
            )

            Row(
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        imageUri = null
                        alreadyExecuted = false
                        textVal.value = ""
                        launcher.launch("image/*")
                    },
                    shape = RoundedCornerShape(55)
                ) {
                    Text(text = "Choose new photo")
                }

                Spacer(modifier = Modifier.width(5.dp))

                Button(
                    onClick = { showDialog = !showDialog },
                    modifier = Modifier.wrapContentSize(),
                    enabled = textVal.value.isNotEmpty(),
                    shape = RoundedCornerShape(55)
                )
                {
                    Text(text = "Save")
                }
            }

            Text(
                text = textVal.value,
                modifier = Modifier
                    .verticalScroll(verticalScroll)
                    .padding(bottom = 36.dp),
            )

            if (showDialog) {
                SharedPreferences.getCurrLocation()
                SaveTextDialog(
                    onDismiss = { showDialog = !showDialog },
                    hiltViewModel(),
                    textVal.value,
                    imageUri!!
                )
            }

            if(!alreadyExecuted) {
                inputType.let { input ->
                    when (input) {
                        "Text field" -> {
                            imageProcessViewModel.analyzeImageText(InputImage.fromFilePath(context, imageUri!!))
                            textRecognizerFlow.value.let {
                                when (it) {
                                    is Resource.Success -> {
                                        textVal.value = it.result.text
                                        alreadyExecuted = true
                                        textToSpeechViewModel.textToSpeech(context, textVal.value)
                                    }
                                    is Resource.Failure -> {
                                        alreadyExecuted = true
                                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                                        textToSpeechViewModel.textToSpeech(context, it.exception.message!!)
                                    }
                                    is Resource.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(10.dp),
                                            color = MaterialTheme.colors.primaryVariant
                                        )
                                    }
                                }
                            }
                        }
                        "Barcode" -> {
                            imageProcessViewModel.scanBarcode(InputImage.fromFilePath(context, imageUri!!))
                            barcodeScannerFlow.value.let {
                                when (it) {
                                    is Resource.Success -> {
                                        textVal.value = it.result
                                        alreadyExecuted = true
                                        textToSpeechViewModel.textToSpeech(context, textVal.value)
                                    }
                                    is Resource.Failure -> {
                                        alreadyExecuted = true
                                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                                    }
                                    is Resource.Loading -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .size(10.dp),
                                            color = MaterialTheme.colors.primaryVariant
                                        )
                                    }
                                }
                            }

                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }
    else if(textVal.value == ""){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 24.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            launcher.launch("image/*")
                        },
                    )
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ){
            Box(
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(55)
                )
                {
                    Text(text = "Chose image")
                }
            }
        }
    }
}