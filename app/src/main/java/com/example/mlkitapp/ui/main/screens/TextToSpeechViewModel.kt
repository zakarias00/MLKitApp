package com.example.mlkitapp.ui.main.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.Locale

class TextToSpeechViewModel: ViewModel() {

    private lateinit var textToSpeech: TextToSpeech

    fun textToSpeech(context: Context, text: String) {
        textToSpeech = TextToSpeech(
            context
        ) {
            Log.i("texttospeech", it.toString())
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech.let { txtToSpeech ->
                    txtToSpeech.language = Locale.US
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }
    }
}