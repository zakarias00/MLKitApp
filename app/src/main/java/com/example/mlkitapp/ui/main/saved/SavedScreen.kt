package com.example.mlkitapp.ui.main.saved

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mlkitapp.data.utils.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.ui.main.saved.component.TextCard
import com.example.mlkitapp.ui.main.saved.viewmodel.CloudDbViewModel
import com.example.mlkitapp.ui.main.texttospeech.TextToSpeechViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(InternalComposeApi::class)
@Composable
fun SavedScreen(
    dbViewModel: CloudDbViewModel,
    navigationController: NavController
) {
    val textsFlow = dbViewModel.getDocumentsFlow.value
    var textList: List<RecognizedText> = mutableListOf()

    val textToSpeechViewModel: TextToSpeechViewModel = viewModel()
    val savedContext = LocalContext.current

    textsFlow.let { texts ->
        when (texts) {
            is Resource.Success -> {
                textList = filterList(texts.result.toMutableList())
            }
            is Resource.Failure -> {
                Toast.makeText(LocalContext.current, texts.exception.message, Toast.LENGTH_LONG).show()
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(10.dp),
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }

    if(textList.isEmpty()){
        //textToSpeechViewModel.textToSpeech(savedContext, "Now you are on saved screen, but you do not have have any saved items yet")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You does not have any saved items yet!",
                modifier = Modifier
                    .padding(36.dp),
                color = Color.LightGray,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
        }
    }
    else{
        //textToSpeechViewModel.textToSpeech(savedContext, "Now you are on saved screen, you have ${textList.size} items saved. Click on the item to open it's details, or click on the trash icon to delete it")

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(textList.size) { index ->
                if (textList[index].userId == FirebaseAuth.getInstance().currentUser!!.uid) {
                    TextCard(
                        dbViewModel,
                        textList[index],
                        navigationController
                    )
                }
            }
        }
    }
}

fun filterList(list: List<RecognizedText>): List<RecognizedText>{
    return list.filter {
        it.userId == FirebaseAuth.getInstance().currentUser!!.uid
    }.sortedBy {
        it.address.toString()
    }
}