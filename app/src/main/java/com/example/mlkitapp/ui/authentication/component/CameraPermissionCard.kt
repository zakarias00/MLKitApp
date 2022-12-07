package com.example.mlkitapp.ui.authentication.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@SuppressLint("CommitPrefEdits")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionCard(
    permissionType: Int,
    @StringRes titleTextRes: Int,
    @StringRes infoTextRes: Int,
    @StringRes buttonTextRes: Int,
    cameraPermissionState: PermissionState,
    context: Context
){

    Box(
        Modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            backgroundColor = Color.White,
            elevation = 24.dp,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(320.dp)
                    .wrapContentHeight(),
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
                    Button(
                        onClick = {
                        if (permissionType == 0) {
                            cameraPermissionState.launchPermissionRequest()
                        } else {
                            onOpenSettingsClick(context)
                        }
                    },
                        shape = RoundedCornerShape(55.dp)
                    ) {
                        Text(
                            stringResource(id = buttonTextRes)
                        )
                    }
                }
            }
        }
    }
}

private fun onOpenSettingsClick(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}
