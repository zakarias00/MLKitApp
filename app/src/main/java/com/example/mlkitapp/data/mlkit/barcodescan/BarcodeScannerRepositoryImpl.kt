package com.example.mlkitapp.data.mlkit.barcodescan

import android.annotation.SuppressLint
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BarcodeScannerRepositoryImpl @Inject constructor(): BarcodeScannerRepository {

    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override suspend fun scanBarCode(inputImage: InputImage): Flow<String> = callbackFlow{
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    trySend(barcode.rawValue.toString())
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                throw it
            }
            .addOnCompleteListener  { close() }
        awaitClose { close() }
    }
}