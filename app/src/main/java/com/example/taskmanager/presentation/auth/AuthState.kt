package com.example.taskmanager.presentation.auth

data class AuthState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)