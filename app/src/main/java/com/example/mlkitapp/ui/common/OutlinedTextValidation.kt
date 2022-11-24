package com.example.mlkitapp.ui.common

import android.util.Patterns
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.unit.dp
import com.example.mlkitapp.ui.authentication.utils.Email
import com.example.mlkitapp.ui.authentication.utils.EmptyEmail
import com.example.mlkitapp.ui.authentication.utils.EmptyPassword
import com.example.mlkitapp.ui.authentication.utils.InputValidator
import com.example.mlkitapp.ui.authentication.utils.Password

class OutlinedTextValidation(val name: String, val inLabel: String = "", val inputValidators: List<InputValidator>){
    var text: String by mutableStateOf("")
    var label: String by mutableStateOf(inLabel)
    var hasError: Boolean by mutableStateOf(false)

    fun clear() { text = "" }

    private fun showError(error: String){
        hasError = true
        label = error
    }

    private fun hideError(){
        hasError = false
        label = inLabel
    }

    @Composable
    fun Content(){
        TextField(
            value = text,
            isError = hasError,
            label = { Text(text = label) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent {
//                    if (it.isFocused) {
//                        coroutineScope.launch {
//                            bringIntoViewRequester.bringIntoView()
//                        }
//                    }
                },
            onValueChange = { value ->
                hideError()
                text = value
            },
            shape = RoundedCornerShape(55.dp)
        )
    }

    fun validate(): Boolean {
        return inputValidators.map {
            when(it) {
                is Email -> {
                    if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                        showError(it.message)
                        return@map false
                    }
                    true
                }
                is EmptyEmail -> {
                    if(text.isEmpty()){
                        showError(it.message)
                        return@map false
                    }
                    true
                }
                is Password -> {
                    if(text.count() < 8){
                        showError(it.message)
                        return@map false
                    }
                    true
                }
                is EmptyPassword-> {
                    if(text.isEmpty()){
                        showError(it.message)
                        return@map false
                    }
                    true
                }
            }
        }.all { it }
    }

}





//
//@Composable
//fun OutlinedTextFieldValidation(
//    value: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    readOnly: Boolean = false,
//    textStyle: TextStyle = LocalTextStyle.current,
//    label: @Composable (() -> Unit)? = null,
//    placeholder: @Composable (() -> Unit)? = null,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    error: String = "",
//    isError: Boolean = error.isNotEmpty(),
//    trailingIcon: @Composable (() -> Unit)? = {
//        if (error.isNotEmpty())
//            Icon(Icons.Default.Close, "error", tint = MaterialTheme.colors.error)
//    },
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    singleLine: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
//    shape: Shape = MaterialTheme.shapes.small,
//    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
//        disabledTextColor = Color.Black
//    )
//
//) {
//
//    Column {
//        OutlinedTextField(
//            enabled = enabled,
//            readOnly = readOnly,
//            value = value,
//            onValueChange = onValueChange,
//            modifier = Modifier
//                .fillMaxWidth(),
//            singleLine = singleLine,
//            textStyle = textStyle,
//            label = label,
//            placeholder = placeholder,
//            leadingIcon = leadingIcon,
//            trailingIcon = trailingIcon,
//            isError = isError,
//            visualTransformation = visualTransformation,
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,
//            maxLines = maxLines,
//            interactionSource = interactionSource,
//            shape = shape,
//            colors = colors
//        )
//        if (error.isNotEmpty()) {
//            Text(
//                text = error,
//                color = MaterialTheme.colors.error,
//                style = MaterialTheme.typography.caption,
//                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
//            )
//        }
//    }
//}