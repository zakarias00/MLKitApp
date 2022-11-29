package com.example.mlkitapp.data.database

import android.net.Uri
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import kotlinx.coroutines.flow.Flow

interface CloudDbRepository {
    fun getDocuments(): Flow<Resource<MutableList<RecognizedText>>?>
    suspend fun saveDocumentAndImage(uId: String, address: String, recText: String, lat: Double, long: Double, private: Boolean, imageUri: Uri): Flow<Resource<Void?>>
    suspend fun editDocument(documentId: String, private: Boolean): Flow<Resource<Void?>>
    suspend fun deleteDocument(documentId: String, imageUrl: String): Flow<Resource<Void?>>
}