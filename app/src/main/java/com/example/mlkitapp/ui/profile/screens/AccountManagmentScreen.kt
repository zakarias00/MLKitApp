package com.example.mlkitapp.ui.profile.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AccountManagerScreen(
    authViewModel: AuthViewModel,
    back: () -> Unit
) {
    BackHandler(onBack = back)

    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val provider = currentUser!!.providerData[currentUser.providerData.size - 1].providerId

    val providerName = provider.let {
        when (it) {
            "password" -> {
                "email address and password"
            }
            "google.com" -> {
                "Google account"
            }

            else -> {
                ""
            }
        }
    }

    val providerIcon = provider.let {
        when (it) {
            "password" -> {
                R.drawable.ic_email
            }
            "google.com" -> {
                R.drawable.icon_google
            }
            else -> {
                R.drawable.ic_email
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        Icon(
            painterResource(id = providerIcon),
            contentDescription = providerName,
            tint = Color.Unspecified
        )

        Text(
            "You are signed in with your $providerName",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )


        Button(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .align(CenterHorizontally)
                .wrapContentSize(),
            shape = RoundedCornerShape(55),
            enabled = provider == "password",
            onClick = {
                currentUser.email?.let { authViewModel.resetPassword(it) }

                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                context.startActivity(Intent.createChooser(intent, "Email"))
            }
        ) {
            Text(text = "Change password")

        }

        if (provider != "password") {
            Text(
                text = "Note: You cannot change your password, because you are signed in via socials!",
                fontSize = 14.sp
            )
        }
    }
}



