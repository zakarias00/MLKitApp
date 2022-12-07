package com.example.mlkitapp.ui.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitapp.data.Resource
import com.example.mlkitapp.data.auth.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    private val _credentialLoginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val credentialLoginFlow: StateFlow<Resource<FirebaseUser>?> = _credentialLoginFlow

    private val _resetPasswordFlow = MutableStateFlow<Resource<Void>?>(null)
    val resetPasswordFlow: StateFlow<Resource<Void>?> = _resetPasswordFlow

    private val _signInProvider = MutableStateFlow("")
    val signInProvider: StateFlow<String> = _signInProvider

    private val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = Resource.Success(repository.currentUser!!)
            getSignInProvider()
        }
    }

    private fun getSignInProvider(){
        _signInProvider.value = currentUser!!.providerData[currentUser!!.providerData.size - 1].providerId
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun loginWithCreds(credential: AuthCredential) = viewModelScope.launch {
        _credentialLoginFlow.value = Resource.Loading
        val result = repository.loginWithCreds(credential)
        _credentialLoginFlow.value = result
    }


    fun signup(email: String, password: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = repository.signUp(email, password)
        _signupFlow.value = result
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _resetPasswordFlow.value = Resource.Loading
        val result = repository.recoverPassword(email)
        _resetPasswordFlow.value = result
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
        _credentialLoginFlow.value = null
    }
}