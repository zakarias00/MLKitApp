package com.example.mlkitapp.ui.main.saved.component

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mlkitapp.data.utils.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.ui.main.saved.viewmodel.CloudDbViewModel
import com.example.mlkitapp.ui.main.texttospeech.TextToSpeechViewModel
import com.example.mlkitapp.ui.nav.routes.NAV_CLICKED_ITEM

@OptIn(InternalComposeApi::class)
@Composable
fun TextCard(
    dbViewModel: CloudDbViewModel,
    recognizedText: RecognizedText,
    navController: NavController?,
    tssViewModel: TextToSpeechViewModel = viewModel(),
) {
    val cardContext = LocalContext.current
    val deleteFlow = dbViewModel.deleteDocumentFlow.collectAsState()

    var showToast by remember { mutableStateOf(true) }

    val itemIsPrivate = if(recognizedText.private == true) "private" else "public"

    val itemInfo = "Post's information: location is ${recognizedText.address.toString()}" +
            "Recognized text is ${recognizedText.recognizedText.toString()}" +
            "Your post is ${itemIsPrivate}, you can change it, by tapping on the switch." +
            "You can open the post in map, by clicking the button at the bottom of the screen."

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                SharedPreferences.addSharedRecognizedText(recognizedText)
                navController!!.navigate(NAV_CLICKED_ITEM)
                tssViewModel.textToSpeech(cardContext, itemInfo)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = recognizedText.address.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.weight(2F)
            )

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                onClick = {
                    showToast = true
                    dbViewModel.deleteDocument(recognizedText.id!!, recognizedText.imageUri!!)
                },
                modifier = Modifier.weight(0.5F)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete button",
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        deleteFlow.value.let { deleteResult ->
            when (deleteResult) {
                is Resource.Success -> {
                    //if (showToast) {
                        Toast.makeText(cardContext, "Successfully deleted item!", Toast.LENGTH_LONG).show()
                  //  }
//                    deleteFlow.value = Resource.Loading
                }
                is Resource.Failure -> {
                    //if (showToast) {
                        Toast.makeText(cardContext, deleteResult.exception.message, Toast.LENGTH_LONG).show()
                   // }
                    showToast = false
                }
                is Resource.Loading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .padding(top = 36.dp)
//                            .size(4.dp),
//                        color = MaterialTheme.colors.primaryVariant
//                    )
                }

            }
        }

    }


}
