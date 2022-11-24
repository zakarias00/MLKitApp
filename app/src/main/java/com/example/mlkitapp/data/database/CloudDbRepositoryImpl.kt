package com.example.mlkitapp.data.database

import android.net.Uri
import android.util.Log
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date


@Singleton
class CloudDbRepositoryImpl @Inject constructor(
    private val dbReference: CollectionReference,
    private val storage: FirebaseStorage,
) : CloudDbRepository {

    override fun getDocuments() = callbackFlow {
        val snapshotListener = dbReference.addSnapshotListener { snapshot, e ->
            val response =
                if (snapshot != null) {
                    val recognizedTexts = snapshot.toObjects(RecognizedText::class.java)
                    Log.i("anna", recognizedTexts[0].toString())
                    Resource.Success(recognizedTexts)
                }
                else {
                    e?.let { Resource.Failure(it) }
                }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun saveDocument(uId: String, title: String, recText: String, lat: Double, long: Double, private: Boolean) = flow {
        try {
            emit(Resource.Loading)
            val recordId = dbReference.document().id
            val recognizedText = RecognizedText(
                id = recordId,
                userId = uId,
                title = title,
                recognizedText = recText,
                latitude = lat,
                longitude = long,
                private = private
            )
            val saveOperation = dbReference.document(recordId).set(recognizedText).result
            emit(Resource.Success(saveOperation))
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }


    override suspend fun editDocument(documentId: String, private: Boolean) = flow {
        try{
            emit(Resource.Loading)
            val editOperation = dbReference.document(documentId).update("private", private).await()
            emit(Resource.Success(editOperation))
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }


    override suspend fun deleteDocument(documentId: String) = flow {
        try {
            emit(Resource.Loading)
            val delOperation = dbReference.document(documentId).delete().await()
            emit(Resource.Success(delOperation))
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    override suspend fun saveDocumentAndImage(uId: String, title: String, recText: String, lat: Double, long: Double, private: Boolean, imageUri: Uri) = callbackFlow {
        val currentDate = Date().time
        val storageReference = storage.reference.child("images")
        storageReference.putFile(imageUri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { url ->
                val documentId = dbReference.document().id
                dbReference.document(documentId).set(
                    hashMapOf(
                        "id" to documentId,
                        "userId" to uId,
                        "title" to title,
                        "recognizedText" to recText,
                        "latitude" to lat,
                        "longitude" to long,
                        "private" to private,
                        "imageUri" to url.toString()
                    )
                ).addOnSuccessListener {
                    trySend(Resource.Success(it))
                }.addOnFailureListener {
                    trySend(Resource.Failure(it))
                }
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        }.addOnFailureListener {
            trySend(Resource.Failure(it))
        }.addOnCompleteListener { close() }
        awaitClose { close() }

    }
//        return channelFlow {
//            val result = MutableStateFlow<Resource<Void>?>(null)
//
//            val documentId = dbReference.document().id
//            val recognizedText = RecognizedText(id = documentId, userId = uId, title = title, recognizedText = recText, latitude = lat, longitude = long, private = private)
//
//            val uploadOp = storage.reference.child("images").child("$title.jpg").putFile(imageUri).await()
//            val downloadedUrl =  uploadOp.storage.downloadUrl.await()
//            if(downloadedUrl != null) {
//                GlobalScope.launch {
//                    recognizedText.imageUri = downloadedUrl
//                    val saveOp = dbReference.document(documentId).set(recognizedText)
//                    if(saveOp.isSuccessful) {
//                        result.value = Resource.Success(saveOp.result)
//                    }
//                    else{
//                        result.value = Resource.Failure(saveOp.exception!!)
//                    }
//                }
//            }
//            else{
//                result.value = Resource.Failure(uploadOp.error!!)
//            }
//
//            awaitClose {
//                trySend(result.value!!)
//                channel.close()
//            }
//
//        }

}
