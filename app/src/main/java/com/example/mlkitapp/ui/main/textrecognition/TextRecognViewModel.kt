package com.example.mlkitapp.ui.main.textrecognition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.mlkit.textrecogn.TextRecognRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class TextRecognViewModel @Inject constructor(
    private val repository: TextRecognRepository
) : ViewModel() {

    private val _recognizedTextFlow = MutableStateFlow<Resource<Text>?>(null)
    val recognizedTextFlow : StateFlow<Resource<Text>?> = _recognizedTextFlow

    init {
        if(repository.recognizedText != null) {
            _recognizedTextFlow.value = Resource.Success(repository.recognizedText!!)
        }
    }

    fun analyzeImage(image: InputImage) = viewModelScope.launch {
        _recognizedTextFlow.value = Resource.Loading
        repository.analyzeImage(image)
            .flowOn(Dispatchers.IO)
            .collect{
                _recognizedTextFlow.value = Resource.Success(it)
            }
    }

}