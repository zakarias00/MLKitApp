package com.example.mlkitapp.ui.main.profile.screens

import android.content.Intent
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mlkitapp.R
import com.example.mlkitapp.ui.authentication.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AccountManagerScreen(
    authViewModel: AuthViewModel,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val provider = authViewModel.signInProvider.collectAsState()

    val providerName = provider.let {
        when (it.value) {
            "password" -> {
                R.string.email_and_password
            }
            "google.com" -> {
                R.string.google_account
            }
            else -> {
                R.string.empty
            }
        }
    }

    val providerIcon = provider.let {
        when (it.value) {
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
            contentDescription = stringResource(id = providerName),
            tint = Color.Unspecified
        )

        Text(
            text = stringResource(id = R.string.signed_in_via) + stringResource(id = providerName),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )

        Button(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .align(CenterHorizontally)
                .wrapContentSize(),
            shape = RoundedCornerShape(55),
            enabled = provider.value == "password",
            onClick = {
                currentUser!!.email?.let { authViewModel.resetPassword(it) }

                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                context.startActivity(Intent.createChooser(intent, "Email"))
            }
        ) {
            Text(text = stringResource(id = R.string.change_password))
        }
        if (provider.value != "password") {
            Text(
                text = stringResource(id = R.string.cant_change_password),
                fontSize = 14.sp
            )
        }
    }
}



