package com.example.mlkitapp.ui.authentication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.ui.BaseActivity
import com.example.mlkitapp.ui.theme.MLKitAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirebaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitAppTheme {
                AuthNavHost(rememberNavController())
            }
        }
    }
}