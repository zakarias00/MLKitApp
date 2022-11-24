package com.example.mlkitapp.di

import com.example.mlkitapp.data.auth.AuthRepository
import com.example.mlkitapp.data.auth.AuthRepositoryImpl
import com.example.mlkitapp.data.contants.Constants.CollectionName
import com.example.mlkitapp.data.database.CloudDbRepository
import com.example.mlkitapp.data.database.CloudDbRepositoryImpl
import com.example.mlkitapp.data.mlkit.barcodescan.BarcodeScannerRepository
import com.example.mlkitapp.data.mlkit.barcodescan.BarcodeScannerRepositoryImpl
import com.example.mlkitapp.data.mlkit.textrecogn.TextRecognRepository
import com.example.mlkitapp.data.mlkit.textrecogn.TextRecognRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class Providers {
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseDb(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideAuthRepository(implementation: AuthRepositoryImpl): AuthRepository = implementation

    @Provides
    fun provideTextRecognitionRepository(implementation: TextRecognRepositoryImpl): TextRecognRepository = implementation

    @Provides
    fun provideBarcodeScannerRepository(implementation: BarcodeScannerRepositoryImpl): BarcodeScannerRepository = implementation

    @Provides
    fun provideTextReference(db: FirebaseFirestore) = db.collection(CollectionName)

    @Provides
    fun provideRecordRepository(textReference: CollectionReference, storage: FirebaseStorage): CloudDbRepository = CloudDbRepositoryImpl(textReference, storage)
}