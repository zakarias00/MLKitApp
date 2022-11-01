package com.example.mlkitapp.ui.main.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.ui.common.TextCard
import com.example.mlkitapp.ui.main.db.CloudDbViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(InternalComposeApi::class)
@Composable
fun SavedScreen(
    dbViewModel: CloudDbViewModel
) {
    val textsFlow = dbViewModel.getRecordsFlow.value
    var textList : List<RecognizedText> = mutableListOf()

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Saved",
                        style = MaterialTheme.typography.h5
                    )
                }
            }
            items(textList.size) { index ->
                TextCard(textList[index].recognizedText!!, textList[index].latitude!!, textList[index].longitude!!, textList[index].imageUrl!!)
            }
        }
    }


    textsFlow.let { texts ->
        when(texts){
            is Resource.Success -> {
                textList = texts.result.toMutableList()
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
