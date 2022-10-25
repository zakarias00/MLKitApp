package com.example.mlkitapp.data.auth

import com.example.mlkitapp.data.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(email: String, password: String): Resource<FirebaseUser>
    suspend fun loginWithCreds(credential: AuthCredential): Resource<FirebaseUser>
    fun logout()
}