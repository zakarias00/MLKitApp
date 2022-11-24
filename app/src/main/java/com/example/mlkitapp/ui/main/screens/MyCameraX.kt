package com.example.mlkitapp.ui.main.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mlkitapp.R
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.common.CameraPermissionCard
import com.example.mlkitapp.ui.common.SaveTextDialog
import com.example.mlkitapp.ui.main.barcodescan.BarcodeScannerViewModel
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.common.InputImage
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, InternalComposeApi::class)
@Composable
fun ImagePicker(
    inputType: String,
    textRecognViewModel: TextRecognViewModel,
    barcodeScannerViewModel: BarcodeScannerViewModel,
    textToSpeechViewModel: TextToSpeechViewModel = viewModel()
) {
    val context = LocalContext.current

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    PermissionRequired(
        permissionState = cameraPermissionState,

        permissionNotGrantedContent = {
            CameraPermissionCard(
                titleTextRes = R.string.camera_permission_title,
                infoTextRes = R.string.camera_permission_info,
                buttonTextRes = R.string.camera_permission_button,
                cameraPermissionState = cameraPermissionState,
                permissionType = 0,
                context = context
            )
        },
        permissionNotAvailableContent = {
            CameraPermissionCard(
                titleTextRes = R.string.camera_permission_disabled_title,
                infoTextRes = R.string.camera_permission_disabled_info,
                buttonTextRes = R.string.camera_permission_disabled_button,
                cameraPermissionState = cameraPermissionState,
                permissionType = 1,
                context = context
            )
        },
    ) {
        var hasImage by remember {
            mutableStateOf(false)
        }
        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                Log.i("anna", success.toString())
                hasImage = success
            }
        )

        val textVal = remember {
            mutableStateOf(" ")
        }
        val verticalScroll = rememberScrollState(0)

        val buttonText = inputType.let {
            when (it) {
                "Text field" -> {
                    "Recognize text"
                }
                "Barcode" -> {
                    "Scan barcode"
                }
                else -> {
                    ""
                }
            }
        }

        val recognizedTextFlow = textRecognViewModel.recognizedTextFlow.collectAsState()
        val barcodeScannerFlow = barcodeScannerViewModel.barcodeFlow.collectAsState()

        var showDialog by remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (hasImage && imageUri != null) {
                Image(
                    rememberAsyncImagePainter(model = ImageRequest.Builder(context)
                        .crossfade(false)
                        .data(imageUri)
                        .build(),
                    filterQuality = FilterQuality.High),
                    contentDescription = "Taken image",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                )

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        shape = RoundedCornerShape(55),
                        onClick = {
                            val uri = ComposeFileProvider.getImageUri(context)
                            Log.i("anna", uri.toString())
                            imageUri = uri
                            cameraLauncher.launch(uri)
                            //hasImage = true
                            textVal.value = ""
                        }) {
                        Text(text = "Take new photo")
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    Button(
                        onClick = {
                            textRecognViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri!!))
                        },
                        shape = RoundedCornerShape(55)
                    )
                    {
                        Text(text = buttonText)
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

                Button(
                    onClick = {
                        textToSpeechViewModel.textToSpeech(context, textVal.value)

                    },
                    shape = RoundedCornerShape(55)
                )
                {
                    Text(text = "Read")
                }

                Text(
                    text = textVal.value,
                    modifier = Modifier.verticalScroll(verticalScroll)
                )

                recognizedTextFlow.value.let {
                    when (it) {
                        is Resource.Success -> {
                            textVal.value = it.result.text
                        }
                        is Resource.Failure -> {
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

                barcodeScannerFlow.value.let {
                    when (it) {
                        is Resource.Success -> {
                            textVal.value = it.result
                        }
                        is Resource.Failure -> {
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

                if (showDialog) {
                    SaveTextDialog(
                        onDismiss = { showDialog = !showDialog },
                        hiltViewModel(),
                        textVal.value,
                        imageUri!!
                    )
                }

            } else {
                Box(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = {
                            val uri = ComposeFileProvider.getImageUri(context)
                            imageUri = uri
                            cameraLauncher.launch(uri)
                        },
                        modifier = Modifier.wrapContentSize(),
                        shape = RoundedCornerShape(55)
                    )
                    {
                        Text(text = "Take photo")
                    }
                }
            }
        }
    }
}