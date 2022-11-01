package com.example.mlkitapp.ui.common

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
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
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.main.db.CloudDbViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(InternalComposeApi::class)
@Composable
fun SaveTextDialog(
    onDismiss: () -> Unit,
    dbViewModel: CloudDbViewModel,
    recognizedText: String,
    imageUrl: String,
) {
    val saveTextFlow = dbViewModel.saveRecordFlow
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

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
                .padding(8.dp),
            elevation = 10.dp
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Do you want to save this to the cloud?",
                    modifier = Modifier.padding(top = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "Note: If you save this to the cloud the other users will be able to see it.\n" +
                            "If you don/t want this, you can save it just for yourself.",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 8.sp
                )

                Row(Modifier.padding(top = 4.dp)) {
                    Button(
                        onClick = {
                            dbViewModel.saveRecord(
                                currentUserId,
                                recognizedText,
                                0.0,
                                0.0,
                                false,
                                imageUrl
                            )
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Save to the cloud", fontSize = 10.sp)
                    }

                    Button(
                        onClick = {
                            dbViewModel.saveRecord(
                                currentUserId,
                                recognizedText,
                                0.0,
                                0.0,
                                true,
                                imageUrl
                            )},
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Save for myself", fontSize = 10.sp)
                    }
                }
            }
        }

        saveTextFlow.value.let { saveResult ->
            when(saveResult){
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(10.dp)
                    )
                }
                is Resource.Failure -> {
                    Toast.makeText(LocalContext.current, saveResult.exception.message, Toast.LENGTH_LONG).show()
                }
                 is Resource.Success -> {
                    Log.i("User",  currentUserId)
                 }
            }
        }
    }
}

//private fun uploadPostWithImage(imageUrl: String, context: Context) {
//    val bitmap: Bitmap = (imageUrl as BitmapDrawable).bitmap
//
//
//    try {
//        val url = URL(imageUrl)
//        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//    } catch (e: IOException) {
//        System.out.println(e)
//    }
//    val baos = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//    val imageInBytes = baos.toByteArray()
//
//    val storageReference = FirebaseStorage.getInstance().reference
//    val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
//    val newImageRef = storageReference.child("images/$newImageName")
//
//
//}


@Preview
@Composable
fun SaveTextDialogPreview(){
    //SaveTextDialog(onDismiss = {}, ", ", ",")
}