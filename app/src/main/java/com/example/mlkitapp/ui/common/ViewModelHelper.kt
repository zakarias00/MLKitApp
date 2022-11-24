package com.example.mlkitapp.ui.common

import android.util.Log
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.ui.main.barcodescan.BarcodeScannerViewModel
import com.example.mlkitapp.ui.main.textrecognition.TextRecognViewModel
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ViewModelHelper(
    private val textRecognViewModel: TextRecognViewModel?,
    private val barcodeScannerViewModel: BarcodeScannerViewModel?
) {
    private var _resultFlow = MutableStateFlow<Resource<String>?>(null)
    val resulFlow : StateFlow<Resource<String>?> = _resultFlow
    private val recognizedTextFlow = textRecognViewModel!!.recognizedTextFlow
    private val barcodeScannerFlow = barcodeScannerViewModel?.barcodeFlow
    fun getResult(inputImage: InputImage) {
        if(textRecognViewModel != null){

            textRecognViewModel.analyzeImage(inputImage)

            recognizedTextFlow.value.let {
                Log.i("result", it.toString())
                when (it) {
                    is Resource.Success -> {
                        _resultFlow.value = Resource.Success(it.result.text)
                    }
                    is Resource.Failure -> {
                        _resultFlow.value = Resource.Failure(it.exception)
                    }
                    is Resource.Loading -> {
                        _resultFlow.value = Resource.Loading
                    }
                }
            }
        }
        else if(barcodeScannerViewModel != null){

            barcodeScannerViewModel.analyzeImage(inputImage)


            barcodeScannerFlow?.value.let {
                when (it) {
                    is Resource.Success -> {
                        _resultFlow.value = Resource.Success(it.result)
                    }
                    is Resource.Failure -> {
                        _resultFlow.value = Resource.Failure(it.exception)
                    }
                    else -> {
                        Resource.Loading
                    }
                }
            }
        }
    }
}