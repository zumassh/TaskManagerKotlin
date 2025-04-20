package com.example.taskmanager.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val auth = FirebaseAuth.getInstance()

    fun onAuthSuccess() {
        _state.value = _state.value.copy(isLoading = false)
    }

    fun onAuthFailed(message: String?) {
        _state.value = _state.value.copy(isLoading = false, errorMessage = message)
    }

    fun startLoading() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}
