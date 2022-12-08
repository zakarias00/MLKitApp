package com.example.mlkitapp.ui.main.imageprocess.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitapp.data.database.CloudDbRepository
import com.example.mlkitapp.data.mlkit.barcodescan.BarcodeScannerRepository
import com.example.mlkitapp.data.mlkit.textrecogn.TextRecognRepository
import com.example.mlkitapp.data.utils.Resource
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class ImageProcessViewModel @Inject constructor(
    private val textRecognRepository: TextRecognRepository,
    private val barcodeScannerRepository: BarcodeScannerRepository,
    private val cloudDbRepository: CloudDbRepository
): ViewModel() {

    private val _barcodeFlow = MutableStateFlow<Resource<String>?>(Resource.Loading)
    val barcodeFlow : StateFlow<Resource<String>?> = _barcodeFlow

    private val _recognizedTextFlow = MutableStateFlow<Resource<Text>?>(Resource.Loading)
    val recognizedTextFlow : StateFlow<Resource<Text>?> = _recognizedTextFlow

    private val _saveDocumentFlow = MutableStateFlow<Resource<Void?>>(Resource.Loading)
    val saveDocumentFlow: StateFlow<Resource<Void?>> = _saveDocumentFlow

    private val _recognizedText = MutableStateFlow<Text?>(null)
    val recognizedText: StateFlow<Text?> = _recognizedText


    fun analyzeImageText(image: InputImage) = viewModelScope.launch {
        textRecognRepository.analyzeImage(image)
            .flowOn(Dispatchers.IO)
            .collect{
                _recognizedTextFlow.value = Resource.Success(it)
            }
    }

    fun scanBarcode(inputImage: InputImage) = viewModelScope.launch {
        barcodeScannerRepository.scanBarCode(inputImage)
            .flowOn(Dispatchers.IO)
            .collect{
                _barcodeFlow.value = Resource.Success(it)
            }
    }

    fun saveDocumentAndImage(userId: String, address: String, text: String, lat: Double, long: Double, isPrivate: Boolean, imageUri: Uri) = GlobalScope.launch{
        cloudDbRepository.saveDocumentAndImage(userId, address, text, lat, long, isPrivate, imageUri)
            .flowOn(Dispatchers.IO)
            .collect{
                _saveDocumentFlow.value = it
            }
    }

}