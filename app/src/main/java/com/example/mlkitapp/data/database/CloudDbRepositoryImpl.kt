package com.example.mlkitapp.data.database

import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CloudDbRepositoryImpl @Inject constructor(
    private val dbReference: CollectionReference,
) : CloudDbRepository {

    override fun getRecords() = callbackFlow {
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

    override suspend fun saveRecord(uId: String, recText: String, lat: Double, long: Double, private: Boolean, url : String) = flow {
        try {
            emit(Resource.Loading)
            val recordId = dbReference.document().id
            val recognizedText = RecognizedText(
                id = recordId,
                userId = uId,
                recognizedText = recText,
                latitude = lat,
                longitude = long,
                isPrivate = private,
                imageUrl = url
            )
            val saveOperation = dbReference.document(recordId).set(recognizedText).await()
            emit(Resource.Success(saveOperation))
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    override suspend fun editRecord(recognizedText: RecognizedText): Flow<Resource<Void?>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecord(recordId: String) = flow {
        try {
            emit(Resource.Loading)
            val delOperation = dbReference.document(recordId).delete().await()
            emit(Resource.Success(delOperation))
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}