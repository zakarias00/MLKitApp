package com.example.mlkitapp.viewmodel

import android.annotation.SuppressLint
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MLKitViewModel: ViewModel() {

    private lateinit var textRecognizer: TextRecognizer
    private var textRecognitionResult: MutableLiveData<String> = MutableLiveData()
    private var failureResult: MutableLiveData<Exception> = MutableLiveData()
    var showOutput: MutableLiveData<Boolean> = MutableLiveData()

    private var loading : MutableState<Boolean> = mutableStateOf(false)

    init {
        initializeMLKit()
    }

    private fun initializeMLKit() {
        val instance = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    fun getResult(): LiveData<String>{
        return textRecognitionResult
    }

    fun getFailureResult(): LiveData<Exception>{
        return failureResult
    }

    fun getLoading(): MutableState<Boolean>{
        return loading
    }

    private fun setLoading(actualLoading: Boolean){
        this.loading.value = actualLoading
    }

    fun resetFailureResult(){
        this.failureResult.value = null
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun analyze(imageProxy: ImageProxy) {
        setLoading(true)
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            textRecognizer.process(image).apply {
                    addOnSuccessListener{
                        setLoading(false)
                        textRecognitionResult.value = it.text
                        showOutput.value = true
                        imageProxy.close()
                    }
                    addOnFailureListener {
                        setLoading(false)
                        failureResult.value = it
                        imageProxy.close()
                    }
                }
                /*.addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.value = it.result?.text
                    }
                    imageProxy.close()
                }*/


        }
    }
}