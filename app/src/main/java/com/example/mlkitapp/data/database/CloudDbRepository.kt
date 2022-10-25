package com.example.mlkitapp.data.database

import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.models.RecognizedText
import kotlinx.coroutines.flow.Flow

interface CloudDbRepository {
    fun getRecords(): Flow<Resource<MutableList<RecognizedText>>?>
    suspend fun saveRecord(uId: String, recText: String, lat: Double, long: Double, private: Boolean, url : String): Flow<Resource<Void?>>
    suspend fun editRecord(recognizedText: RecognizedText): Flow<Resource<Void?>>
    suspend fun deleteRecord(recordId: String): Flow<Resource<Void?>>
}