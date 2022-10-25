package com.example.mlkitapp.ui.authentication.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mlkitapp.ui.main.MainActivity
import com.example.mlkitapp.R
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.authentication.AuthViewModel
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
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
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
                        value = email,
                        label = {
                            Text(text = "Email")
                        },
                        onValueChange = {
                            email = it
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
                        },

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
                                        contentDescription = "visibility icon"
                                    )
                                }
                            } else {
                                IconButton(onClick = { showConfirmPassword = true }) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_visibility_off),
                                        contentDescription = "visibility off icon"
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

                    Button(
                        modifier = Modifier
                            .width(160.dp)
                            .height(50.dp),
                        enabled = email.isNotEmpty() && password.isNotEmpty() && password == confirmedPassword,
                        content = {
                            Text(text = "Registration")
                        },
                        onClick = {
                            viewModel.signup(email.trim(), email.trim())
                        },
                        shape = RoundedCornerShape(45)
                    )
                    signupFlow.value?.let {
                        when (it) {
                            is Resource.Failure -> {
                                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                            }
                            Resource.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(10.dp)
                                )
                            }
                            is Resource.Success -> {
                                LaunchedEffect(Unit) {
                                    context.startActivity(Intent(context, MainActivity::class.java))
                                }
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
