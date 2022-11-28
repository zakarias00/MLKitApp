package com.example.mlkitapp.ui.authentication.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mlkitapp.R
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.main.nav.routes.NAV_MAIN_SCREEN
import com.example.mlkitapp.ui.main.nav.routes.NAV_SIGNUP
import com.example.mlkitapp.ui.main.screens.TextToSpeechViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegistrationScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    textToSpeechViewModel: TextToSpeechViewModel = viewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }

    var isEmailFieldError by rememberSaveable { mutableStateOf(false) }
    var isPasswordFieldError by rememberSaveable { mutableStateOf(false) }
    var areTwoPasswordsEqualError by rememberSaveable { mutableStateOf(false) }
    var emailErrorMessageRes by remember { mutableStateOf(R.string.empty) }
    var passwordErrorMessageRes by rememberSaveable { mutableStateOf(R.string.empty) }
    var confirmedPasswordErrorMessageRes by rememberSaveable { mutableStateOf(R.string.empty) }

    fun validateEmail(text: String){
        if(text.isEmpty()){
            emailErrorMessageRes = R.string.empty_email_field
            isEmailFieldError = true
        }
        else if(!text.contains("@")) {
            emailErrorMessageRes = R.string.wrong_email_field
            isEmailFieldError = true
        } else if(!text.contains(".")){
            emailErrorMessageRes = R.string.wrong_email_field_without_domain
            isEmailFieldError = true
        }else{
            isEmailFieldError = false
        }
    }

    fun validatePassword(text: String) {
        if(text.isEmpty()){
            isPasswordFieldError = true
            passwordErrorMessageRes = R.string.empty_password_field
        } else if(text.length < 8) {
            isPasswordFieldError = true
            passwordErrorMessageRes = R.string.short_password_field
        } else{
            isPasswordFieldError = false
        }
    }

    fun validateTwoPasswords(pw1: String, pw2: String) {
        if(pw1 != pw2){
            areTwoPasswordsEqualError = true
            confirmedPasswordErrorMessageRes = R.string.not_equal_passwords
        }
        else {
            areTwoPasswordsEqualError = false
        }
    }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val signupFlow = authViewModel.signupFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()

    val context = LocalContext.current

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
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
                    .align(CenterHorizontally),
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
                value = email,
                label = {
                    Text(
                        text = stringResource(id = R.string.email_field_label)
                    )
                },
                onValueChange = {
                    email = it
                    isEmailFieldError = it.contains("@")
                    if(isEmailFieldError){
                        textToSpeechViewModel.textToSpeech(context, getString(context, emailErrorMessageRes))
                    }
                },
                singleLine = true,
                isError = isEmailFieldError,
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                    //isEmailFieldError = false
                    validateEmail(email)

                }
            )
            if (isEmailFieldError) {
                Text(
                    text = stringResource(id = emailErrorMessageRes),
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
                shape = RoundedCornerShape(55),
                value = password,
                label = {
                    Text(
                        text = stringResource(id = R.string.password_field_label)
                    )
                },
                onValueChange = {
                    password = it
                    validatePassword(it)

                },
                isError = isPasswordFieldError,
                keyboardOptions = KeyboardOptions(
                    FocusDirection.Down
                ),
                keyboardActions = KeyboardActions (
                    onSend = {
                        textToSpeechViewModel.textToSpeech(context, getString(context, passwordErrorMessageRes))
                        //}
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }, trailingIcon = {
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
            if (isPasswordFieldError) {
                Text(
                    text = stringResource(id = passwordErrorMessageRes),
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
                shape = RoundedCornerShape(55),
                value = confirmedPassword,
                label = {
                    Text(
                        text = stringResource(id = R.string.password_confirm_field_label)
                    )
                },
                isError = areTwoPasswordsEqualError,
                onValueChange = {
                    confirmedPassword = it
                    validateTwoPasswords(it, password)
                    if(areTwoPasswordsEqualError){
                        textToSpeechViewModel.textToSpeech(context, getString(context, confirmedPasswordErrorMessageRes))
                    }
                },
                singleLine = true,
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                visualTransformation = if (showConfirmPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    if (showConfirmPassword) {
                        IconButton(onClick = { showConfirmPassword = false }) {
                            Icon(
                                painterResource(id = R.drawable.ic_visibility),
                                contentDescription = stringResource(id = R.string.visibility_icon),
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    } else {
                        IconButton(onClick = { showConfirmPassword = true }) {
                            Icon(
                                painterResource(id = R.drawable.ic_visibility_off),
                                contentDescription = stringResource(id = R.string.visibility_off_icon),
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                },
            )
            if (areTwoPasswordsEqualError) {
                Text(
                    text = stringResource(id = confirmedPasswordErrorMessageRes),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty() && password == confirmedPassword,
                content = {
                    Text(
                        text = stringResource(id = R.string.registration_button_text)
                    )
                },
                onClick = {
                    authViewModel.signup(email.trim(), email.trim())
                },
                shape = RoundedCornerShape(55),
                border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                modifier = Modifier
                    .wrapContentSize()
                    .align(CenterHorizontally),
                content = {
                    Text(
                        text = stringResource(id = R.string.cancel_button_text)
                    )
                },
                onClick = {
                    navController.navigate(NAV_LOGIN) {
                        popUpTo(NAV_SIGNUP) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(55),
                border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant)
            )
            signupFlow.value?.let {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    }
                    Resource.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(10.dp),
                            color = MaterialTheme.colors.primaryVariant
                        )
                    }
                    is Resource.Success -> {
                        navController.navigate(NAV_MAIN_SCREEN)
                    }
                }
            }
        }
    }
}

private fun getString(context: Context, stringRes: Int): String{
    return context.resources.getString(stringRes)
}