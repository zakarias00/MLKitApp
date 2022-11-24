package com.example.mlkitapp.ui.common

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mlkitapp.data.utils.SharedObject
import com.example.mlkitapp.ui.main.db.CloudDbViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth


@OptIn(InternalComposeApi::class)
@Composable
fun SaveTextDialog(
    onDismiss: () -> Unit,
    dbViewModel: CloudDbViewModel,
    recognizedText: String,
    imageUrl: Uri
) {
    val saveTextFlow = dbViewModel.saveDocumentFlow.collectAsState()
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val context = LocalContext.current

    val currLoc = SharedObject.getCurrLocation()

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                IconButton(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .size(28.dp)
                ){
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Do you want to save this to \n" + "the cloud?",
                    modifier = Modifier.padding(10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = "Note: If you save this to the cloud the other users will be able to see it.\n" +
                            "If you don/t want this, you can save it just for yourself.",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 12.sp
                )

                Row(Modifier.padding(top = 4.dp)) {
                    Button(
                        onClick = {
                            dbViewModel.saveDocumentAndImage(
                                currentUserId,
                                getAddress(LatLng(currLoc.latitude, currLoc.longitude), context)!!,
                                recognizedText,
                                currLoc.latitude,
                                currLoc.longitude,
                                false,
                                imageUrl
                            )
                            onDismiss()
                        },
                        Modifier
                            .wrapContentSize()
                            .padding(4.dp)
                            .weight(1F) ,
                        shape = RoundedCornerShape(55.dp),
                        enabled = (currLoc.latitude != 0.0 && currLoc.longitude != 0.0)
                    ) {
                        Text(text = "Save to the cloud", fontSize = 10.sp)
                    }

                    Button(
                        onClick = {
                            dbViewModel.saveDocumentAndImage(
                                currentUserId,
                                getAddress(LatLng(currLoc.latitude, currLoc.longitude), context)!!,
                                recognizedText,
                                currLoc.latitude,
                                currLoc.longitude,
                                true,
                                imageUrl
                            )
                                onDismiss()
                            },
                        Modifier
                            .wrapContentSize()
                            .padding(4.dp)
                            .weight(1F),
                        shape = RoundedCornerShape(55.dp),
                        enabled = (currLoc.latitude != 0.0 && currLoc.longitude != 0.0)
                        ) {
                        Text(text = "Save for myself", fontSize = 10.sp)
                    }
                }
            }
        }

//        saveTextFlow.value.let { saveResult ->
//            when(saveResult){
//                is Resource.Loading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier
//                            .size(10.dp),
//                        color = MaterialTheme.colors.primaryVariant
//                    )
//                }
//                is Resource.Failure -> {
//                    Toast.makeText(LocalContext.current, saveResult.exception.message, Toast.LENGTH_LONG).show()
//                }
//                 is Resource.Success -> {
//                     Toast.makeText(LocalContext.current, "The item was successfully saved!", Toast.LENGTH_LONG).show()
//                     onDismiss()
//                 }
//            }
//        }
    }
}

private fun getAddress(lat: LatLng, context: Context): String? {
    val geocoder = Geocoder(context)
    val list = geocoder.getFromLocation(lat.latitude, lat.longitude, 1)
    return list[0].getAddressLine(0)
}

@Preview
@Composable
fun SaveTextDialogPreview(){
}