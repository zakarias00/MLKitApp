package com.example.mlkitapp.ui.profile.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mlkitapp.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(18.dp))
            
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOpenSettingsClick(context)
                }
                .padding(all = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Info,
                contentDescription = "Info icon",
                tint = MaterialTheme.colors.primaryVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {

                    Text(
                        text = "App information",
                        style = TextStyle(
                            fontSize = 18.sp,
                        )
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "General application information",
                        style = TextStyle(
                            fontSize = 14.sp,
                            letterSpacing = (0.8).sp,
                            color = Color.Gray
                        )
                    )

                }

                Icon(
                    modifier = Modifier
                        .weight(weight = 0.5f, fill = false),
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "Arrow-forward icon"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOpenAccessibilitySettingsClick(context)
                }
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {


            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_accessibility),
                contentDescription = "Accessibility icon",
                tint = MaterialTheme.colors.primaryVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {

                    Text(
                        text = "Accessibility settings",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "Control your application's accessibility",
                        style = TextStyle(
                            fontSize = 14.sp,
                            letterSpacing = (0.8).sp,
                            color = Color.Gray
                        )
                    )
                }

                Icon(
                    modifier = Modifier
                        .weight(weight = 0.5f, fill = false),
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "Arrow-forward icon"
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOpenLocationSettingsClick(context)
                }
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_locations),
                contentDescription = "Location icon",
                tint = MaterialTheme.colors.primaryVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "Location settings",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "Control your application's location settings",
                        style = TextStyle(
                            fontSize = 14.sp,
                            letterSpacing = (0.8).sp,
                            color = Color.Gray
                        )
                    )
                }

                Icon(
                    modifier = Modifier
                        .weight(weight = 0.5f, fill = false),
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "Arrow-forward icon"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOpenLocationSettingsClick(context)
                }
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_talk_back),
                contentDescription = "Talkback icon",
                tint = MaterialTheme.colors.primaryVariant
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "Talkback settings",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = "Control your application's talkback audio settings",
                        style = TextStyle(
                            fontSize = 14.sp,
                            letterSpacing = (0.8).sp,
                            color = Color.Gray
                        )
                    )
                }

                Icon(
                    modifier = Modifier
                        .weight(weight = 0.5f, fill = false),
                    imageVector = Icons.Outlined.ArrowForward,
                    contentDescription = "Arrow-forward icon"
                )
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

private fun onOpenLocationSettingsClick(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

private fun onOpenAccessibilitySettingsClick(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}
