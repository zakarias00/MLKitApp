package com.example.mlkitapp.data.mlkit.textrecogn

import android.annotation.SuppressLint
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class TextRecognRepositoryImpl @Inject constructor() : TextRecognRepository {

    override var recognizedText: Text? = null

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun analyzeImage(imageProxy: InputImage): Flow<Text> = callbackFlow{
        textRecognizer.process(imageProxy)
            .addOnSuccessListener {
                trySend(it)
            }
            .addOnFailureListener {
                it.printStackTrace()
                throw it
            }
            .addOnCompleteListener { close() }
        awaitClose { close() }
    }
}