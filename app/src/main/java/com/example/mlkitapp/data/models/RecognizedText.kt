package com.example.mlkitapp.data.models

data class RecognizedText (
    val id: String? = null,
    val title: String? = null,
    val userId: String? = null,
    val recognizedText: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val private: Boolean? = null,
    var imageUri: String? = null
)
