package com.example.mlkitapp.ui.common

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.example.mlkitapp.data.utils.SharedObject
import com.example.mlkitapp.ui.main.db.CloudDbViewModel
import com.example.mlkitapp.ui.main.nav.routes.NAV_CLICKED_ITEM

@OptIn(InternalComposeApi::class)
@Composable
fun TextCard(
    dbViewModel: CloudDbViewModel,
    recognizedText: RecognizedText,
    navController: NavController?
) {
    val context = LocalContext.current
    val deleteFlow = dbViewModel.deleteDocumentFlow.collectAsState()

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                SharedObject.addSharedRecognizedText(recognizedText)
                navController!!.navigate(NAV_CLICKED_ITEM)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = recognizedText.title.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.weight(2F)
            )

            Spacer(modifier = Modifier.weight(1F))

            IconButton(
                onClick = {
                    dbViewModel.deleteDocument(recognizedText.id!!)
                          },
                modifier = Modifier.weight(0.5F)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete button",
                    tint = MaterialTheme.colors.primary
                )
            }
            deleteFlow.value.let {
                when (it) {
                    is Resource.Success -> {
                        //Toast.makeText(context, "Successfully deleted item!", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Failure -> {
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
    }


}
