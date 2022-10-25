package com.example.mlkitapp.ui.main.screens

import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.main.db.CloudDbViewModel

@OptIn(InternalComposeApi::class)
@Composable
fun SavedScreen(
    dbViewModel: CloudDbViewModel
) {
    val textsFlow = dbViewModel.getRecordsFlow.value
    textsFlow.let { texts ->
        when(texts){
            is Resource.Success -> {
                Text(text = texts.toString())
            }
            is Resource.Failure -> {
                Toast.makeText(LocalContext.current, texts.exception.message, Toast.LENGTH_LONG).show()
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(10.dp)
                )
            }
        }
    }
}