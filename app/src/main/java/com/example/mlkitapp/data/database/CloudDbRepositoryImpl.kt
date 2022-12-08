package com.example.mlkitapp.data.database

import android.net.Uri
import com.example.mlkitapp.data.utils.Resource
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

    override suspend fun editDocument(documentId: String, private: Boolean) = flow {
        try{
            emit(Resource.Loading)
            val editOperation = dbReference.document(documentId).update("private", private).await()
            emit(Resource.Success(editOperation))
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }


    override suspend fun deleteDocument(documentId: String, imageUrl: String) = callbackFlow {

        trySend(Resource.Loading)
        dbReference.document(documentId).delete().addOnSuccessListener {
            storage.reference.child("images").child(getImageNameFromUrl(imageUrl)).delete().addOnCanceledListener {
                trySend(Resource.Success(it))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        }.addOnFailureListener {
            trySend(Resource.Failure(it))
        }.addOnCompleteListener { close() }
        awaitClose { close() }

    }

    override suspend fun saveDocumentAndImage(
        uId: String, address: String, recText: String, lat: Double, long: Double, private: Boolean, imageUri: Uri
    ) = callbackFlow {
        val currentDate = Date().time
        val storageReference = storage.reference.child("images").child("${uId+currentDate}.jpg")
        storageReference.putFile(imageUri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { url ->
                val documentId = dbReference.document().id
                dbReference.document(documentId).set(
                    hashMapOf(
                        "id" to documentId,
                        "userId" to uId,
                        "address" to address,
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
}


private fun getImageNameFromUrl(url: String): String {
    val firstPart = url.substringBefore("?alt")
    val secondPart = firstPart.substringAfter("%2F")
    return secondPart.replace("%20", " ")
}