@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.mlkitapp.ui.main.screens

import android.graphics.Point
import android.graphics.RectF
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.utils.SharedObject
import com.example.mlkitapp.ui.common.SaveTextDialog
import com.example.mlkitapp.ui.main.barcodescan.BarcodeScannerViewModel
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import kotlin.math.pow
import kotlin.math.sqrt


@OptIn(InternalComposeApi::class)
@Composable
fun GalleryScreen(
    inputType: String,
    textRecognViewModel: TextRecognViewModel,
    barcodeScannerViewModel: BarcodeScannerViewModel,
    textToSpeechViewModel: TextToSpeechViewModel = viewModel(),
) {
    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult( contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    var textVal by remember { mutableStateOf("") }
    val verticalScroll = rememberScrollState(0)

    var textBlocks = remember { mutableListOf<Text.TextBlock>() }

    val textRecognizerFlow = textRecognViewModel.recognizedTextFlow.collectAsState()
    val barcodeScannerFlow = barcodeScannerViewModel.barcodeFlow.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var alreadyExecuted by remember { mutableStateOf(false) }

    if (imageUri != null) {
Log.i("anna", alreadyExecuted.toString())

        if(!alreadyExecuted) {
            //delay(2000)
            inputType.let {
                when (it) {
                    "Text field" -> {
                        textRecognViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri!!))
                    }
                    "Barcode" -> {
                        barcodeScannerViewModel.analyzeImage(InputImage.fromFilePath(context, imageUri!!))
                    }
                    else -> {
                        ""
                    }
                }
            }
            alreadyExecuted = true
        }

        Column(
            modifier = Modifier.fillMaxSize(),
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
                    //.size(200.dp)
                    /*.drawBehind {
                        if (textBlocks.size > 0) {
                            for (i in textBlocks) {
                                val lines = i.lines
                                for (j in lines) {

                                    val linePoints = j.cornerPoints
                                    val offsets: List<Offset> = linePoints!!.map {
                                        Offset(it.x.toFloat(), it.y.toFloat())

                                    }
                                    drawRect(
                                        color = Color.Red,
                                        size = Size(j.boundingBox!!.width().toFloat(), j.boundingBox!!.height().toFloat())
                                    )

                                    val elements = j.elements
                                    for (k in elements) {
                                        val frame = k.boundingBox
                                        val points = k.cornerPoints



                                        offsets.forEach {
                                            Log.i("anna", it.toString())
                                        }

//                                        if (points != null) {
//                                            for(p in points) {
//                                                Log.i("anna", "X:" + p.x.toString() + "Y:" + p.y.toString())
//                                            }
//                                        }
                                        //if(points != null) {


//                                        val height = calculateDistance(points[3], points[0])
//                                        val width = calculateDistance(points[3], points[2])
//                                        val ofset: Offset

//                                            drawRect(
//                                                color = Color.Blue,
//                                                //topLeft = Offset(points[3].x.toFloat(),points[0].x.toFloat()),
//                                                style = Stroke(width = 8f),
//                                                size = Size(height.toFloat(), height.toFloat())
//                                            )
                                    }
                                }
                            }

                            // }
                        }


                    }*/
                    .padding(16.dp),
            )

            Row(
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        textVal = ""
                        alreadyExecuted = false
                        textBlocks = emptyList<Text.TextBlock>().toMutableList()

                        launcher.launch("image/*")
                    },
                    shape = RoundedCornerShape(55)
                ) {
                    Text(text = "Choose new photo")
                }

                Spacer(modifier = Modifier.width(5.dp))

                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = { showDialog = !showDialog },
                    modifier = Modifier.wrapContentSize(),
                    enabled = textVal.isNotEmpty(),
                    shape = RoundedCornerShape(55)
                )
                {
                    Text(text = "Save")
                }

            }

            Button(
                onClick = {
                    textToSpeechViewModel.textToSpeech(context, textVal)
                },
                shape = RoundedCornerShape(55)
            )
            {
                Text(text = "Read")
            }
            Text(
                text = textVal,
                modifier = Modifier
                    .verticalScroll(verticalScroll)
                    .padding(bottom = 36.dp),
            )

            textRecognizerFlow.value.let {
                when (it) {
                    is Resource.Success -> {
                        textVal = it.result.text
                        textBlocks = it.result.textBlocks
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
                    else -> {
                        textVal = ""
                    }
                }
            }

            barcodeScannerFlow.value.let {
                when (it) {
                    is Resource.Success -> {
                        textVal = it.result
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
                    else -> {}
                }
            }

            if (showDialog) {
                SharedObject.getCurrLocation()
                SaveTextDialog(
                    onDismiss = { showDialog = !showDialog },
                    hiltViewModel(),
                    textVal,
                    imageUri!!
                )
            }
        }
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ){
                Box(
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Button(
                        onClick = {
                            alreadyExecuted = false
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

private fun calculateDistance(p1: Point, p2: Point) : Double{
    return sqrt((p1.x - p2.x).toDouble().pow(2.0) + (p1.y - p2.y).toDouble().pow(2.0))
}

private fun translateRect(rect: Rect) : RectF {
    val scaleX = 200.dp.value
    val scaleY = 200.dp.value

    return RectF(
        rect.left * scaleX,
        rect.top * scaleY,
        rect.right * scaleX,
        rect.bottom * scaleY
    )
}


@Preview
@Composable
fun GalleryScreenPreview() {
    //GalleryScreen()
}