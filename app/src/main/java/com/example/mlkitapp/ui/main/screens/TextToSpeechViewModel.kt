package com.example.mlkitapp.ui.main.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import com.example.mlkitapp.data.utils.SharedPreferences
import java.util.Locale

class TextToSpeechViewModel: ViewModel() {

    private lateinit var textToSpeech: TextToSpeech

    private val isEnabled = SharedPreferences.isTextToSpeechEnabled.value

    fun textToSpeech(context: Context, text: String) {
        if(isEnabled) {
            textToSpeech = TextToSpeech(
                context
            ) {
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
}