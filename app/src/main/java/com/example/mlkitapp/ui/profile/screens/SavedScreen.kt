package com.example.mlkitapp.ui.profile.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.ui.common.TextCard
import com.example.mlkitapp.ui.main.db.CloudDbViewModel
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
    val ok = mutableStateOf(false)

    textsFlow.let { texts ->
        when (texts) {
            is Resource.Success -> {
                textList = texts.result.toMutableList()
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

    if(filterList(textList).isEmpty()){

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


fun filterList(list: List<RecognizedText>): List<RecognizedText>{
    return list.filter {
        it.userId == FirebaseAuth.getInstance().currentUser!!.uid
    }
}
