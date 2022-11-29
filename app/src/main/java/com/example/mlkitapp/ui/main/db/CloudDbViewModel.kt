package com.example.mlkitapp.ui.main.db

import android.net.Uri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
@InternalComposeApi
class CloudDbViewModel @Inject constructor(
    private val repository: CloudDbRepository
): ViewModel(){
    private val _getDocumentsFlow = mutableStateOf<Resource<List<RecognizedText>>>(Resource.Loading)
    val getDocumentsFlow: State<Resource<List<RecognizedText>>> = _getDocumentsFlow

    private val _saveDocumentFlow = MutableStateFlow<Resource<Void?>>(Resource.Loading)
    val saveDocumentFlow: StateFlow<Resource<Void?>> = _saveDocumentFlow

    private val _editDocumentFlow = MutableStateFlow<Resource<Void?>>(Resource.Success(null))
    val editDocumentFlow: StateFlow<Resource<Void?>> = _editDocumentFlow

    private val _deleteDocumentFlow = MutableStateFlow<Resource<Void?>>(Resource.Loading)
    val deleteDocumentFlow: StateFlow<Resource<Void?>> = _deleteDocumentFlow

    init{
        getDocuments()
    }

    private fun getDocuments(){
        viewModelScope.launch {
            repository.getDocuments().collect{
                _getDocumentsFlow.value = it!!
            }
        }
    }

    fun saveDocumentAndImage(userId: String, address: String, text: String, lat: Double, long: Double, isPrivate: Boolean, imageUri: Uri) = GlobalScope.launch{
        repository.saveDocumentAndImage(userId, address, text, lat, long, isPrivate, imageUri)
            .flowOn(Dispatchers.IO)
            .collect{
                _saveDocumentFlow.value = it
        }
    }

    fun deleteDocument(id: String, url: String) = viewModelScope.launch {
        repository.deleteDocument(id, url)
            .flowOn(Dispatchers.IO)
            .collect{
                _deleteDocumentFlow.value = it
            }
        _deleteDocumentFlow.value = Resource.Loading
    }


    fun editDocument(id: String, private: Boolean) = viewModelScope.launch {
        _editDocumentFlow.value = Resource.Loading
        repository.editDocument(id, private)
            .flowOn(Dispatchers.IO)
            .collect{
                _editDocumentFlow.value = it
            }
    }

}
