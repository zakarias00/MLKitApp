package com.example.mlkitapp.data.mlkit.textrecogn

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import kotlinx.coroutines.flow.Flow

interface TextRecognRepository {
    suspend fun analyzeImage(imageProxy: InputImage) : Flow<Text>
}
