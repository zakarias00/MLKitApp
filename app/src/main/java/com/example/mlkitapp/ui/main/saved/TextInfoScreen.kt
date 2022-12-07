package com.example.mlkitapp.ui.main.saved

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.utils.SharedPreferences
import com.example.mlkitapp.ui.main.saved.viewmodel.CloudDbViewModel
import com.example.mlkitapp.ui.main.texttospeech.TextToSpeechViewModel
import com.example.mlkitapp.ui.nav.routes.NAV_CLICKED_ITEM_OPEN_MAP

@OptIn(InternalComposeApi::class)
@Composable
fun TextInfoScreen(
    navController: NavController?,
    dbViewModel: CloudDbViewModel
){
    val context = LocalContext.current

    val editFlow = dbViewModel.editDocumentFlow.collectAsState()
    val recognizedText = SharedPreferences.sharedRecognizedText

    var showToast by remember { mutableStateOf(false) }
    var private by remember { mutableStateOf(false) }

    val textToSpeechViewModel: TextToSpeechViewModel = viewModel()

    Column {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            elevation = 24.dp,
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.72f)
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f, false),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            ) {
            val checkStateValue = recognizedText?.private!!
            val checkedState = remember { mutableStateOf(checkStateValue) }

            val textToRead = "Post's information: location is ${recognizedText.address.toString()}" +
                    "Recognized text is ${recognizedText.recognizedText}" +
                    "Your post is private, you can change it, by tapping on the switch." +
                    "You can open the post in map, by clicking the button at the bottom of the screen."

            textToSpeechViewModel.textToSpeech(context, textToRead)

            Text(
                text = "Location",
                style = MaterialTheme.typography.body1,
                color = Color.DarkGray,
            )

            Text(
                text = recognizedText.address.toString(),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Text",
                style = MaterialTheme.typography.body1,
                color = Color.DarkGray,
            )

            Text(
                text = recognizedText.recognizedText.toString(),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Image",
                style = MaterialTheme.typography.body1,
                color = Color.DarkGray,
            )
            if (recognizedText.imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(recognizedText.imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Recognized text's image"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                style = MaterialTheme.typography.body1,
                text = "Private",
                color = Color.DarkGray,
            )
            Switch(
                checked = checkedState.value,
                onCheckedChange = {
                    showToast = true
                    checkedState.value = it
                    private = it
                    onSwitchClick(dbViewModel, recognizedText.id!!, it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primaryVariant,
                    uncheckedThumbColor = MaterialTheme.colors.primaryVariant,
                    checkedTrackColor = MaterialTheme.colors.primary,
                    uncheckedTrackColor = MaterialTheme.colors.primary,
                    checkedTrackAlpha = 0.8f,
                )
            )

        }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(55.dp),
            onClick = {
                textToSpeechViewModel.textToSpeech(context, "item opened in map")
                navController!!.navigate(NAV_CLICKED_ITEM_OPEN_MAP)
            }
        ) {
            Text(text = "Open in map")
        }
    }
    editFlow.value.let {
        when (it) {
            is Resource.Success -> {
                if (private) {
                    if(showToast) {
                        textToSpeechViewModel.textToSpeech(context, "Now, your saved item is private, that means only you can access it")
                        Toast.makeText(context,
                            "Now, your saved item is private, that means only you can access it!",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    showToast = false
                }
                else{
                    if(showToast) {
                        textToSpeechViewModel.textToSpeech(context, "Now, your saved item is public, that means only every user can access it")
                        Toast.makeText(context,
                            "Now, your saved item is public, that means only every user can access it!",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                    showToast = false
                }
            }
            is Resource.Failure -> {
                textToSpeechViewModel.textToSpeech(context,  it.exception.message.toString())
                Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
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
}

@OptIn(InternalComposeApi::class)
fun onSwitchClick(viewModel: CloudDbViewModel, textId: String, private: Boolean){
    viewModel.editDocument(textId, private)
}
