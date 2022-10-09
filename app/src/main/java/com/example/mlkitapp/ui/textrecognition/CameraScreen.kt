package com.example.mlkitapp.ui.textrecognition

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import com.example.mlkitapp.R
import com.example.mlkitapp.viewmodel.MLKitViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BuildCameraUi(
    context: Context,
    mlKitViewModel: MLKitViewModel,
    cameraScanListener: OpenSettingsInterface
){
    val cameraPermissionState = rememberPermissionState(CAMERA)
        PermissionRequired(
            permissionState = cameraPermissionState,

            permissionNotGrantedContent = {
                PermissionCard(
                    titleTextRes = R.string.camera_permission_title,
                    infoTextRes = R.string.camera_permission_info,
                    buttonTextRes = R.string.camera_permission_button,
                    cameraPermissionState = cameraPermissionState,
                    listener = cameraScanListener,
                    permissionType = 0
                )
            },
            permissionNotAvailableContent = {
                PermissionCard(
                    titleTextRes = R.string.camera_permission_disabled_title,
                    infoTextRes = R.string.camera_permission_disabled_info,
                    buttonTextRes = R.string.camera_permission_disabled_button,
                    cameraPermissionState = cameraPermissionState,
                    listener = cameraScanListener,
                    permissionType = 1
                )
            },
        ) {
            ConstraintLayout(Modifier.fillMaxSize()){
                val (preview, takePhotoButton, progress) = createRefs()
                val executor = remember(context) { ContextCompat.getMainExecutor(context) }
                val imageCapture: MutableState<ImageCapture?> = remember { mutableStateOf(null) }
                val textRecognitionFailure by mlKitViewModel.getFailureResult().observeAsState()

                textRecognitionFailure?.let {
                    Toast.makeText(LocalContext.current, it.message, Toast.LENGTH_LONG).show()
                    mlKitViewModel.resetFailureResult()
                }

                CameraView(
                    modifier = Modifier.constrainAs(preview){
                        linkTo(top = parent.top, bottom = parent.bottom)
                        linkTo(start = parent.start, end = parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                    imageCapture = imageCapture,
                    executor = executor,
                    context = context
                )

                Button(
                    modifier = Modifier.constrainAs(takePhotoButton){
                        linkTo(start = parent.start, end = parent.end)
                        bottom.linkTo(parent.bottom, 16.dp)
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    },
                    onClick = {
                        imageCapture.value?.takePicture( executor,
                            object : ImageCapture.OnImageCapturedCallback(){
                                override fun onCaptureSuccess(image: ImageProxy) {
                                     mlKitViewModel.analyze(image)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                ) {
                        Text(stringResource(id = R.string.camera_permission_button))
                }

                val isLoading = remember{ mlKitViewModel.getLoading() }
                CircularProgressIndicator(
                    modifier = Modifier
                        .constrainAs(progress) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            linkTo(start = parent.start, end = parent.end)
                            width = Dimension.value(80.dp)
                            height = Dimension.value(80.dp)
                        }
                        .then(Modifier.alpha(if (isLoading.value) 1f else 2f))
                )

            }
        }
}

interface OpenSettingsInterface {
    fun onOpenSettingsClick()
}

@Composable
private fun checkPermissions(): Boolean{
    val cameraPermission = ContextCompat.checkSelfPermission(LocalContext.current, CAMERA)
    return cameraPermission == PackageManager.PERMISSION_GRANTED
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionCard(
    permissionType: Int,
    @StringRes titleTextRes: Int,
    @StringRes infoTextRes: Int,
    @StringRes buttonTextRes: Int,
    cameraPermissionState: PermissionState,
    listener: OpenSettingsInterface
){
    Card(
        backgroundColor = Color.White,
        elevation = 24.dp,
        shape = RoundedCornerShape(24.dp),
        modifier =
        Modifier.selectable(
                selected = false,
                onClick = {
                    //listener.onOpenSettingsClick()
                }
            )

    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(320.dp)
                .height(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(id = titleTextRes),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(id = infoTextRes)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = {
                    if (permissionType == 0) {
                        cameraPermissionState.launchPermissionRequest()
                    } else {
                        listener.onOpenSettingsClick()
                    }
                }) {
                    Text(
                        stringResource(id = buttonTextRes)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CameraScreenPreview(){
    //BuildCameraUi(context = LocalContext.current)
}