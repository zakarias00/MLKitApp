package com.example.mlkitapp.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mlkitapp.ui.BaseActivity
import com.example.mlkitapp.ui.main.nav.BottomNavHost
import com.example.mlkitapp.ui.theme.MLKitAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitAppTheme {
                BottomNavHost()
            }
        }
    }

//    override fun onOpenSettingsClick() {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri: Uri = Uri.fromParts("package", packageName, null)
//        intent.data = uri
//        startActivity(intent)
//    }
}


@Composable
fun AppScreen(text: String) = Surface(modifier = Modifier.fillMaxSize()) {
    Text(text = text, fontSize = 32.sp)
}

@Preview
@Composable
fun DefaultPreview() {
   // MainScreen()
}