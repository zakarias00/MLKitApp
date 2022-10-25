package com.example.mlkitapp.ui.main.db

import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.database.CloudDbRepository
import com.example.mlkitapp.data.models.RecognizedText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
@InternalComposeApi
class CloudDbViewModel @Inject constructor(
    private val repository: CloudDbRepository
): ViewModel(){
    private val _getRecordsFlow = mutableStateOf<Resource<List<RecognizedText>>>(Resource.Loading)
    val getRecordsFlow: State<Resource<List<RecognizedText>>> = _getRecordsFlow

    private val _saveRecordFlow = mutableStateOf<Resource<Void?>>(Resource.Success(null))
    val saveRecordFlow: State<Resource<Void?>> = _saveRecordFlow

    private val _deleteRecordFlow = mutableStateOf<Resource<Void?>>(Resource.Success(null))
    val deleteRecordFlow: State<Resource<Void?>> = _deleteRecordFlow

    init{
        getRecords()
    }

    private fun getRecords(){
        viewModelScope.launch {
            repository.getRecords().collect{
                _getRecordsFlow.value = it!!
            }
        }
    }

    fun saveRecord(userId: String, text: String, lat: Double, long: Double, isPrivate: Boolean, imageUrl: String){
        viewModelScope.launch {
            repository.saveRecord(userId, text, lat, long, isPrivate, imageUrl).collect{
                _saveRecordFlow.value = it
            }
        }
    }

    fun deleteRecord(id: String){
        viewModelScope.launch {
            repository.deleteRecord(id).collect{
                _deleteRecordFlow.value = it
            }
        }
    }
}
