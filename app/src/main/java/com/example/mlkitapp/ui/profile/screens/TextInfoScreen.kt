package com.example.mlkitapp.ui.profile.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.utils.SharedObject
import com.example.mlkitapp.ui.main.db.CloudDbViewModel
import com.example.mlkitapp.ui.main.nav.routes.NAV_CLICKED_ITEM_OPEN_MAP

@OptIn(InternalComposeApi::class)
@Composable
fun TextInfoScreen(
    navController: NavController?,
    dbViewModel: CloudDbViewModel = hiltViewModel()
){
    val editFlow = dbViewModel.editDocumentFlow.collectAsState()
    val recognizedText = SharedObject.sharedRecognizedText

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
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f, false),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            val checkStateValue = recognizedText?.private!!
            val checkedState = remember { mutableStateOf(checkStateValue) }

            val textToRead = "Post's informations: location is ${recognizedText.title.toString()}" +
                    "Recognized text is ${recognizedText.recognizedText}" +
                    "Your post is private, you can change it, by tapping on the switch." +
                    "You can open the post in map, by clicking the button at the bottom of the screen."


            Text(
                text = "Location",
                style = MaterialTheme.typography.body1,
                color = Color.DarkGray,
            )

            Text(
                text = recognizedText.title.toString(),
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
                    model = ImageRequest.Builder(LocalContext.current)
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
                    checkedState.value = it
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(55.dp),
            onClick = {
                navController!!.navigate(NAV_CLICKED_ITEM_OPEN_MAP)
            }
        ) {
            Text(text = "Open in map")
        }
    }
    editFlow.value.let {
        when (it) {
//            is Resource.Success -> {
//                if (recognizedText?.isPrivate == true) {
//                    Toast.makeText(LocalContext.current, "Now, your saved item is private, that means only you can access it!", Toast.LENGTH_LONG)
//                        .show()
//                }
//                else{
//                    Toast.makeText(LocalContext.current, "Now, your saved item is public, that means only every user can access it!", Toast.LENGTH_LONG)
//                        .show()
//                }
//            }
//            is Resource.Failure -> {
//                Toast.makeText(LocalContext.current, it.exception.message, Toast.LENGTH_LONG).show()
//            }
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
fun onSwitchClick(viewModel: CloudDbViewModel, textId: String, private: Boolean): Unit{
    viewModel.editDocument(textId, private)
}