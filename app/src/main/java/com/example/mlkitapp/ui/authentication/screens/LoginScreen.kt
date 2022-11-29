package com.example.mlkitapp.ui.authentication.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mlkitapp.R
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.common.ChangePasswordAlertDialog
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.main.nav.routes.NAV_MAIN_SCREEN
import com.example.mlkitapp.ui.main.nav.routes.NAV_SIGNUP
import com.example.mlkitapp.ui.main.screens.TextToSpeechViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    textToSpeechViewModel: TextToSpeechViewModel = viewModel(),
) {

    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    var isEmailFieldEmpty by rememberSaveable { mutableStateOf(false) }
    var isPasswordFieldEmpty by rememberSaveable { mutableStateOf(false) }

    fun validateEmail(text: String){
        isEmailFieldEmpty = text.isEmpty()
    }

    fun validatePassword(text: String) {
        isPasswordFieldEmpty = text.isEmpty()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val loginFlow = authViewModel.loginFlow.collectAsState()
    val loginWithCredentialsFlow = authViewModel.credentialLoginFlow.collectAsState()

    val token = stringResource(R.string.client_secret)

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()
    val context = LocalContext.current

    var showPassword by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    var showToast by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            authViewModel.loginWithCreds(credential)
        } catch (e: ApiException) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Icon(
                painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(id = R.string.logout),
                modifier = Modifier
                    .size(160.dp)
                    .padding(24.dp)
                    .align(Alignment.CenterHorizontally)
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
                shape = RoundedCornerShape(55),
                value = userEmail,
                label = {
                    Text(
                        text = stringResource(id = R.string.email_field_label)
                    )
                },
                onValueChange = {
                    userEmail = it
                    validateEmail(it)
                    if(isEmailFieldEmpty){
                        textToSpeechViewModel.textToSpeech(context, getString(context, R.string.empty_email_field))
                    }
                },
                singleLine = true,
                isError = isEmailFieldEmpty,
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
            )
            if(isEmailFieldEmpty){
                Text(
                    text = stringResource(id = R.string.empty_email_field),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                isError = isPasswordFieldEmpty,
                shape = RoundedCornerShape(55),
                value = userPassword,
                label = {
                    Text(
                        text = stringResource(id = R.string.password_field_label)
                    )
                },
                onValueChange = {
                    userPassword = it
                    validatePassword(it)
                    if(isPasswordFieldEmpty){
                        textToSpeechViewModel.textToSpeech(context, getString(context, R.string.empty_password_field))
                    }
                },
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                singleLine = true,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { showPassword = false }) {
                            Icon(
                                painterResource(id = R.drawable.ic_visibility),
                                contentDescription = stringResource(id = R.string.visibility_icon),
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    } else {
                        IconButton(onClick = { showPassword = true }) {
                            Icon(
                                painterResource(id = R.drawable.ic_visibility_off),
                                contentDescription = stringResource(id = R.string.visibility_off_icon),
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                }
            )
            if(isPasswordFieldEmpty){
                Text(
                    text = stringResource(id = R.string.empty_password_field),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                enabled = userEmail.isNotEmpty() && userPassword.isNotEmpty(),
                content = {
                    Text(
                        text = stringResource(id = R.string.login_button_text)
                    )
                },
                onClick = {
                    showToast = true
                    authViewModel.login(userEmail.trim(), userPassword.trim())
                },
                shape = RoundedCornerShape(55),
                border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDialog = !showDialog
                    },
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.forgotten_password_button_text)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.login_options)
            )
            Spacer(modifier = Modifier.height(8.dp))

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
                            contentDescription = stringResource(id = R.string.google_icon)
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

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption,
                text = stringResource(id = R.string.without_account)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.registration_button_text)
                    )
                },
                onClick = {
                    navController.navigate(NAV_SIGNUP) {
                        popUpTo(NAV_LOGIN) { inclusive = true }

                    }
                },
                shape = RoundedCornerShape(55)
            )

            loginFlow.value.let { emailLogin ->
                when (emailLogin) {
                    is Resource.Failure -> {
                        if(showToast){
                            Toast.makeText(context, emailLogin.exception.message, Toast.LENGTH_LONG).show()
                        }
                        showToast = false
                    }
                    is Resource.Success -> {
                        LaunchedEffect(Unit) {
                            navController.navigate(NAV_MAIN_SCREEN)
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
                                if(showToast){

                                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                                }
                                showToast = false
                            }
                            is Resource.Success -> {
                                LaunchedEffect(Unit) {
                                    navController.navigate(NAV_MAIN_SCREEN)
                                }
                            }
                            else -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(10.dp),
                                    color = MaterialTheme.colors.primaryVariant
                                )
                            }
                        }
                    }
                }
            }

            if(showDialog){
                ChangePasswordAlertDialog(
                    onDismiss = { showDialog = !showDialog },
                    context = context,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

private fun getString(context: Context, stringRes: Int): String{
    return context.resources.getString(stringRes)
}

