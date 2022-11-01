package com.example.mlkitapp.ui.profile

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mlkitapp.ui.BaseActivity
import com.example.mlkitapp.ui.profile.nav.ProfileNavHost
import com.example.mlkitapp.ui.theme.MLKitAppTheme

class ProfileActivity : BaseActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLKitAppTheme {
                ProfileNavHost(rememberNavController())
            }
        }
    }
}