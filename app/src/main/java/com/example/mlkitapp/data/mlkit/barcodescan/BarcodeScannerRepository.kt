package com.example.mlkitapp.data.mlkit.barcodescan

import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.Flow

interface BarcodeScannerRepository {
    suspend fun scanBarCode(inputImage: InputImage): Flow<String>
}
