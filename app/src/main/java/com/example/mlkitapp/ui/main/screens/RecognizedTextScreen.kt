package com.example.mlkitapp.ui.main.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.main.barcodescan.BarcodeScannerViewModel
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel
import com.google.mlkit.vision.common.InputImage
import java.io.ByteArrayOutputStream


@Composable
fun RecognizedTextScreen(
    textRecognViewModel: TextRecognViewModel,
    barcodeScannerViewModel: BarcodeScannerViewModel,
    bitmap: Bitmap?,
) {
    val context = LocalContext.current

    val textVal = remember {
        mutableStateOf(" ")
    }
    val recognizedTextFlow = textRecognViewModel.recognizedTextFlow.collectAsState()
    val barcodeScannerFlow = barcodeScannerViewModel.barcodeFlow.collectAsState()

    val scrollState = rememberScrollState(0)

    var showDialog by remember {
        mutableStateOf(false)
    }

    val imageUri = getImageUri(context, bitmap!!)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
//            rememberAsyncImagePainter(
//                model = ImageRequest.Builder(context)
//                    .crossfade(false)
//                    .data(imageUri)
//                    .build(),
//                filterQuality = FilterQuality.High
//            ),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "capture_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .wrapContentWidth()
                .size(420.dp)
                .padding(5.dp)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Button(onClick = {
                textRecognViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri))
            })
            {
                Text(text = "Text")
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(onClick = {
                barcodeScannerViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri))
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
            text = textVal.value,
            modifier = Modifier
                .verticalScroll(scrollState)
                .then(Modifier.padding(8.dp))
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
                            .size(10.dp)
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
                            .size(10.dp)
                    )
                }
            }
        }
    }
}

private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "image", null)
    return Uri.parse(path)
}
