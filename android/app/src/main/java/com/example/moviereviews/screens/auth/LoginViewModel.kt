package com.example.moviereviews.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.AuthRepository
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun login() {
        val state = _uiState.value

        if (state.isLoading) {
            return
        }

        if (state.email.isBlank() || !state.email.contains("@")) {
            _uiState.update { it.copy(error = "Ingresa un correo valido") }
            return
        }

        if (state.password.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa tu contrasena") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = authRepository.signIn(
                email = state.email.trim(),
                password = state.password
            )) {
                is Result.Success -> _uiState.update { it.copy(isLoading = false) }

                is Result.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = result.message)
                }
            }
        }
    }
}
