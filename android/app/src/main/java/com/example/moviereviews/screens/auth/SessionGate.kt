package com.example.moviereviews.screens.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviereviews.utils.BackendConfig

@Composable
fun SessionGate(content: @Composable () -> Unit) {
    if (BackendConfig.isExpress) {
        content()
        return
    }

    val sessionViewModel: SessionViewModel = hiltViewModel()
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsStateWithLifecycle()
    var showRegister by rememberSaveable { mutableStateOf(false) }

    when (isLoggedIn) {
        null -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        false -> if (showRegister) {
            RegisterScreen(onBack = { showRegister = false })
        } else {
            LoginScreen(onRegisterClick = { showRegister = true })
        }

        true -> content()
    }
}
