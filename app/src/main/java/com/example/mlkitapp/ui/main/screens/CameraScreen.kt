package com.example.mlkitapp.ui.main.screens

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.common.CameraPermissionCard
import com.example.mlkitapp.ui.common.CameraView
import com.example.mlkitapp.ui.main.barcodescan.BarcodeScannerViewModel
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.common.InputImage
import java.io.ByteArrayOutputStream

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BuildCameraUi(
    context: Context,
    textRecognViewModel: TextRecognViewModel,
    barcodeScannerViewModel: BarcodeScannerViewModel
){
    val cameraPermissionState = rememberPermissionState(CAMERA)
        PermissionRequired(
            permissionState = cameraPermissionState,

            permissionNotGrantedContent = {
                CameraPermissionCard(
                    titleTextRes = R.string.camera_permission_title,
                    infoTextRes = R.string.camera_permission_info,
                    buttonTextRes = R.string.camera_permission_button,
                    cameraPermissionState = cameraPermissionState,
                    permissionType = 0,
                    context = LocalContext.current
                )
            },
            permissionNotAvailableContent = {
                CameraPermissionCard(
                    titleTextRes = R.string.camera_permission_disabled_title,
                    infoTextRes = R.string.camera_permission_disabled_info,
                    buttonTextRes = R.string.camera_permission_disabled_button,
                    cameraPermissionState = cameraPermissionState,
                    permissionType = 1,
                    context = LocalContext.current
                )
            },
        ) {
            ConstraintLayout(Modifier.fillMaxSize()) {
                val (preview, takePhotoButton, progress) = createRefs()

                val (image, saveButton, textButton, barcodeButton) = createRefs()

                val executor = remember(context) { ContextCompat.getMainExecutor(context) }

                val imageCapture: MutableState<ImageCapture?> = remember { mutableStateOf(null) }

                val imageUrl: MutableState<Bitmap?> = remember { mutableStateOf(null) }

                val showImage = remember { mutableStateOf(false) }

                val scrollState = rememberScrollState(0)

                var showDialog by remember {
                    mutableStateOf(false)
                }

                if (!showImage.value) {
                    CameraView(
                        modifier = Modifier.constrainAs(preview) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            linkTo(start = parent.start, end = parent.end)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                        imageCapture = imageCapture,
                        executor = executor,
                        context = context
                    )

                    IconButton(
                        modifier = Modifier.constrainAs(takePhotoButton) {
                            linkTo(start = parent.start, end = parent.end)
                            bottom.linkTo(parent.bottom, 72.dp)
                            width = Dimension.value(56.dp)
                            height = Dimension.value(56.dp)
                        },
                        onClick = {
                            imageCapture.value?.takePicture(executor,
                                object : ImageCapture.OnImageCapturedCallback() {
                                    @SuppressLint("UnsafeOptInUsageError")
                                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                        imageUrl.value = imageProxy.convertImageProxyToBitmap()
                                        Log.i("proxy", imageUrl.value.toString())
                                        imageProxy.close()
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        if (!exception.equals("Camera is closed!"))
                                            Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            )
                            showImage.value = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_take_photo),
                            contentDescription = "cameraIcon",
                            tint = Color.White
                        )
                    }
                } else {

                    val textVal = remember {
                        mutableStateOf("")
                    }

                    Image(
                        imageUrl.value!!.asImageBitmap(),
                        contentDescription = "captured_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.constrainAs(image) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            linkTo(start = parent.start, end = parent.end)
                        }
                            .wrapContentSize()
                    )

                    Row(
                        modifier = Modifier.constrainAs(saveButton) {
                            linkTo(start = parent.start, end = parent.end)
                            bottom.linkTo(parent.bottom, 72.dp)
                            width = Dimension.value(56.dp)
                            height = Dimension.value(56.dp)
                        }
                    ) {

                        Button(onClick = {
                            textRecognViewModel.analyzeImage(
                                InputImage.fromFilePath(context, getImageUri(context, imageUrl.value!!))
                            )
                        })
                        {
                            Text(text = "Text")
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(onClick = {
                            barcodeScannerViewModel.analyzeImage(
                                InputImage.fromFilePath(context, getImageUri(context, imageUrl.value!!))
                            )
                        })
                        {
                            Text(text = "Barcode")
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Button(
                            onClick = { showDialog = !showDialog },
                            modifier = Modifier.wrapContentSize(),
                        )
                        {
                            Text(text = "Save")
                        }
                    }

                    Text(
                        text = textVal.value,
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .constrainAs(textButton) {
                                linkTo(start = parent.start, end = parent.end)
                                bottom.linkTo(parent.bottom, 72.dp)
                                width = Dimension.value(56.dp)
                            }
                    )
                }
            }
        }
}

@Composable
private fun checkPermissions(): Boolean{
    val cameraPermission = ContextCompat.checkSelfPermission(LocalContext.current, CAMERA)
    return cameraPermission == PackageManager.PERMISSION_GRANTED
}


private fun ImageProxy.convertImageProxyToBitmap(): Bitmap {
    val buffer = planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "image", null)
    return Uri.parse(path)
}