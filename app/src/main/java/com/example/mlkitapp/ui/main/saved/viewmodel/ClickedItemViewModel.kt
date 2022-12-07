package com.example.mlkitapp.ui.main.saved.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitapp.data.models.RecognizedText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ClickedItemViewModel: ViewModel() {
    init {
        viewModelScope.launch {
            sharedRecognizedText.collect{

            }
        }
    }

    private val _sharedRecognizedText = Channel<RecognizedText>()
    val sharedRecognizedText = _sharedRecognizedText.receiveAsFlow()

    fun addSharedRecognizedText(recognizedText: RecognizedText){
        _sharedRecognizedText.trySend(recognizedText)
    }

    //val szia = rememberPreferenceBooleanSettingState()

}