package com.example.mlkitapp.ui.main.textrecognition

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
               //imageBitmap.drawWithRectangle(it.textBlocks)
            }
    }


    fun Bitmap.drawWithRectangle(textBlocks: List<Text.TextBlock>): Bitmap?{
        val bitmap = copy(config, true)
        val canvas = Canvas(bitmap)
        for (i in textBlocks) {
            val lines = i.lines
            for (j in lines) {
                val elements = j.elements
                for (k in elements) {

                    val bounds = k.boundingBox
                    Paint().apply {
                        color = Color.RED
                        style = Paint.Style.STROKE
                        strokeWidth = 4.0f
                        isAntiAlias = true
                        // draw rectangle on canvas
                        if (bounds != null) {
                            canvas.drawRect(
                                bounds,
                                this
                            )

//                        canvas.drawText(thisLabel.toString(),
//                            bounds.left.toFloat(),
//                            bounds.top.toFloat(), this)
//                        }
                        }
                    }
                }
            }
        }
        return bitmap
    }


}