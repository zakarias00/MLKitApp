package com.example.mlkitapp.ui.main.barcodescan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.mlkit.barcodescan.BarcodeScannerRepository
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    private val repository: BarcodeScannerRepository
): ViewModel() {

    private val _barcodeFlow = MutableStateFlow<Resource<String>?>(null)
    val barcodeFlow : StateFlow<Resource<String>?> = _barcodeFlow

    fun analyzeImage(inputImage: InputImage) = viewModelScope.launch {
        _barcodeFlow.value = Resource.Loading
        repository.scanBarCode(inputImage)
            .flowOn(Dispatchers.IO)
            .collect{
                _barcodeFlow.value = Resource.Success(it)
            }
    }

}