package com.example.mlkitapp.data.models

data class RecognizedText(
    val id: String? = null,
    val userId: String? = null,
    val recognizedText: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isPrivate: Boolean = false,
    val imageUrl: String? = null
)
