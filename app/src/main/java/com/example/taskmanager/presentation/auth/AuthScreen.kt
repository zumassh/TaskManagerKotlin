package com.example.taskmanager.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import androidx.compose.material3.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.example.taskmanager.R
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit
) {
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }

    // Если пользователь уже авторизован — сразу переходим
    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            onAuthenticated()
        }
    }

    val launcher = rememberLauncherForActivityResult(FirebaseAuthUIActivityResultContract()) { result ->
        val response = result.idpResponse
        if (result.resultCode == Activity.RESULT_OK) {
            onAuthenticated()
        } else {
            val error = response?.error?.message ?: "Ошибка входа"
        }
    }

    // Провайдеры входа
    val providers = listOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
    )

    LaunchedEffect(Unit) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_logo)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.FirebaseAuthTheme)
            .build()

        launcher.launch(signInIntent)
    }
}
