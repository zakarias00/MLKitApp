package com.example.mlkitapp.ui.authentication.component

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mlkitapp.ui.authentication.viewmodel.AuthViewModel

@Composable
fun ChangePasswordAlertDialog(
    onDismiss: () -> Unit,
    context: Context,
    authViewModel: AuthViewModel
){
    var userEmail by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(4.dp)
                .testTag("PASSWORD_CHANGE_DIALOG_TAG"),
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally

            ){


                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(55),
                    value = userEmail,
                    label = {
                        Text(text = "Email")
                    },
                    onValueChange = {
                        userEmail = it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .align(Alignment.End)
                        .wrapContentSize(),
                    shape = RoundedCornerShape(55),
                    onClick = {
                        if (userEmail.isNotEmpty()) {
                            authViewModel.resetPassword(userEmail)

                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                            context.startActivity(Intent.createChooser(intent, "Email"))
                        } else
                            Toast.makeText(context, "Please enter your email address to be able to reset your password!", Toast.LENGTH_LONG).show()
                    }
                ){
                    Text(text = "SEND")
                }
            }
        }
    }
}

