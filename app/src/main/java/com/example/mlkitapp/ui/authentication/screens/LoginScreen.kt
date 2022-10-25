package com.example.mlkitapp.ui.authentication.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mlkitapp.MainActivity
import com.example.mlkitapp.R
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.main.nav.routes.NAV_SIGNUP
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val loginFlow = viewModel.loginFlow.collectAsState()
    val loginWithCredentialsFlow = viewModel.credentialLoginFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()
    val context = LocalContext.current

    var showPassword by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            viewModel.loginWithCreds(credential)
        } catch (e: ApiException) {
            Log.e("TAG", e.toString())

            Log.e("TAG", e.message.toString())
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                value = userEmail,
                label = {
                    Text(text = "Email")
                },
                onValueChange = {
                    userEmail = it
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                visualTransformation = PasswordVisualTransformation(),
                value = userPassword,
                label = {
                    Text(text = "Password")
                },
                onValueChange = {
                    userPassword = it
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                trailingIcon = {

                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                painterResource(id = R.drawable.ic_visibility),
                                contentDescription = "visibility icon"
                            )
                        }
                    } else {
                        IconButton(onClick = { showPassword = true }) {
                            Icon(
                                painterResource(id = R.drawable.ic_visibility_off),
                                contentDescription = "visibility off icon"
                            )
                        }
                    }
                }
            )

            Button(
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp),
                enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
                content = {
                    Text(text = "Login")
                },
                onClick = {
                    viewModel.login(userEmail.trim(), userPassword.trim())
                },
                shape = RoundedCornerShape(45)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = "Login with"
            )

            val token = stringResource(R.string.client_secret)

            Row {
                IconButton(
                    modifier = Modifier
                        .height(50.dp),
                    onClick = {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()

                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    content = {

                        Icon(
                            painterResource(id = R.drawable.icon_google),
                            tint = Color.Unspecified,
                            contentDescription = "Google authentication",
                        )
                    }

                )

                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    modifier = Modifier
                        .height(50.dp),
                    onClick = {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()

                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        launcher.launch(googleSignInClient.signInIntent)
                    },
                    content = {
                        Icon(
                            painterResource(id = R.drawable.ic_facebook),
                            tint = Color.Unspecified,
                            contentDescription = "Facebook authentication",
                        )
                    }
                )

            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(NAV_SIGNUP) {
                            popUpTo(NAV_LOGIN) { inclusive = true }
                        }
                    }
                    .bringIntoViewRequester(bringIntoViewRequester),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = "If you do not have an account, click here to sign up!"
            )

            loginFlow.value.let { emailLogin ->
                when (emailLogin) {
                    is Resource.Failure -> {
                        Toast.makeText(context, emailLogin.exception.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        LaunchedEffect(Unit) {
                            context.startActivity(Intent(context, MainActivity::class.java))
                        }
                    }
                    is Resource.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(10.dp)
                        )
                    }
                    else -> loginWithCredentialsFlow.value?.let {
                        when (it) {
                            is Resource.Failure -> {
                                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                            }
                            is Resource.Success -> {
                                LaunchedEffect(Unit) {
                                    context.startActivity(Intent(context, MainActivity::class.java))
                                }
                            }
                            else -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){

}
