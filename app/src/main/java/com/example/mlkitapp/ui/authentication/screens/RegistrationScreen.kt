package com.example.mlkitapp.ui.authentication.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mlkitapp.R
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.authentication.AuthViewModel
import com.example.mlkitapp.ui.authentication.utils.Email
import com.example.mlkitapp.ui.authentication.utils.EmptyEmail
import com.example.mlkitapp.ui.common.OutlinedTextValidation
import com.example.mlkitapp.ui.main.nav.routes.NAV_LOGIN
import com.example.mlkitapp.ui.main.nav.routes.NAV_MAIN_SCREEN
import com.example.mlkitapp.ui.main.nav.routes.NAV_SIGNUP
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun RegistrationScreen(viewModel: AuthViewModel, navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val signupFlow = viewModel.signupFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()

    val context = LocalContext.current

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    OutlinedTextValidation(
                        name = "Email",
                        inputValidators = listOf(Email(), EmptyEmail())
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
                            Text(text = "Email")
                        },
                        onValueChange = {
                            email = it
                        },
                        //error = "Field cannot be empty!"
                    )
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
                            Text(text = "Password")
                        },
                        onValueChange = {
                            password = it
                        },
                        visualTransformation = if (showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }, trailingIcon = {
                            if (showPassword) {
                                IconButton(onClick = { showPassword = false }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_visibility),
                                        contentDescription = "visibility icon",
                                        tint = MaterialTheme.colors.primaryVariant
                                    )
                                }
                            } else {
                                IconButton(onClick = { showPassword = true }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_visibility_off),
                                        contentDescription = "visibility off icon",
                                        tint = MaterialTheme.colors.primaryVariant
                                    )
                                }
                            }
                        },

                    )
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
                            Text(text = "Confirm password")
                        },
                        onValueChange = {
                            confirmedPassword = it
                        },
                        visualTransformation = if (showConfirmPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        }, trailingIcon = {
                            if (showConfirmPassword) {
                                IconButton(onClick = { showConfirmPassword = false }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_visibility),
                                        contentDescription = "visibility icon",
                                        tint = MaterialTheme.colors.primaryVariant
                                    )
                                }
                            } else {
                                IconButton(onClick = { showConfirmPassword = true }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_visibility_off),
                                        contentDescription = "visibility off icon",
                                        tint = MaterialTheme.colors.primaryVariant
                                    )
                                }
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                            //.padding(top = 16.dp),
                        enabled = email.isNotEmpty() && password.isNotEmpty() && password == confirmedPassword,
                        content = {
                            Text(
                                text = "REGISTRATION"
                            )
                        },
                        onClick = {
                            viewModel.signup(email.trim(), email.trim())
                        },
                        shape = RoundedCornerShape(55),
                        border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .wrapContentSize(),
                        content = {
                            Text(text = "CANCEL")
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
            )
        }
    )
}

@Preview
@Composable
fun RegistrationScreenPreview(){

}
