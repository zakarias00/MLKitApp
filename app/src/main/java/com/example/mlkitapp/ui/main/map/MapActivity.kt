package com.example.mlkitapp.ui.main.map

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.InternalComposeApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mlkitapp.ui.BaseActivity
import com.example.mlkitapp.ui.main.map.screens.MapScreen
import com.example.mlkitapp.ui.theme.MLKitAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity: BaseActivity() {

    @OptIn(InternalComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MLKitAppTheme {
                MapScreen(
                    hiltViewModel()
                )
            }
        }
    }
}