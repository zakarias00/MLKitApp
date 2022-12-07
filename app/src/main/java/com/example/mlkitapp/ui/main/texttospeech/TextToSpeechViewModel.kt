package com.example.mlkitapp.ui.main.texttospeech

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import java.util.Locale

class TextToSpeechViewModel: ViewModel() {

    private lateinit var textToSpeech: TextToSpeech

    fun textToSpeech(context: Context, text: String) {
        val sharedPrefs = context.getSharedPreferences("isTextToSpeechEnabled", Context.MODE_PRIVATE)
        val isEnabled = sharedPrefs.getString("isTextToSpeechEnabled", null)

        if(isEnabled == "true") {
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