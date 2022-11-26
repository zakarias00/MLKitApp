package com.example.mlkitapp.ui.profile.screens

import android.content.Intent
import android.widget.Toast
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
    authViewModel: AuthViewModel
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val provider = currentUser!!.providerData[currentUser.providerData.size - 1].providerId

    val providerName = provider.let {
        when (it) {
            "password" -> {
                "email address and password"
            }
            "google" -> {
                "Google account"
            }
            else -> {
                "Facebook account"
            }
        }
    }

    val providerIcon = provider.let {
        when (it) {
            "password" -> {
                R.drawable.ic_email
            }
            "google" -> {
                R.drawable.icon_google
            }
            else -> {
                R.drawable.ic_facebook
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
                onClick = {
                    if (provider == "password") {
                        currentUser.email?.let { authViewModel.resetPassword(it) }

                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                        context.startActivity(Intent.createChooser(intent, "Email"))
                    } else
                        Toast.makeText(context,
                            "You cannot change your password, because you are signed in via socials!",
                            Toast.LENGTH_LONG).show()
                }
            ) {
                Text(text = "Change password")

            }
        }

}

