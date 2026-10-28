package com.example.moviereviews.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.AuthRepository
import com.example.moviereviews.data.repository.UserRepository
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = null, error = null) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, usernameError = null, error = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, error = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmError = null, error = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmVisibility() {
        _uiState.update { it.copy(confirmVisible = !it.confirmVisible) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        val nameError = if (state.name.isBlank()) "Ingresa tu nombre" else null
        val usernameError = when {
            state.username.isBlank() -> "Ingresa un nombre de usuario"
            state.username.trim().length < 3 -> "Minimo 3 caracteres"
            else -> null
        }
        val emailError = if (state.email.isBlank() || !state.email.contains("@")) {
            "Ingresa un correo valido"
        } else {
            null
        }
        val passwordError = if (state.password.length < 6) {
            "Minimo 6 caracteres"
        } else {
            null
        }
        val confirmError = if (state.confirmPassword != state.password) {
            "Las contrasenas no coinciden"
        } else {
            null
        }

        _uiState.update {
            it.copy(
                nameError = nameError,
                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmError = confirmError
            )
        }

        return nameError == null && usernameError == null && emailError == null &&
            passwordError == null && confirmError == null
    }

    fun register() {
        if (_uiState.value.isLoading || !validate()) {
            return
        }

        val state = _uiState.value
        val name = state.name.trim()
        val username = state.username.trim().lowercase()
        val email = state.email.trim()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val usernameResult = userRepository.usernameExists(username)) {
                is Result.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = usernameResult.message)
                    }
                    return@launch
                }

                is Result.Success -> {
                    if (usernameResult.data) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                usernameError = "Ese nombre de usuario ya esta en uso"
                            )
                        }
                        return@launch
                    }
                }
            }

            when (val signUpResult = authRepository.signUp(email, state.password)) {
                is Result.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = signUpResult.message)
                    }
                    return@launch
                }

                is Result.Success -> Unit
            }

            val uid = authRepository.currentUserId
            if (uid == null) {
                authRepository.signOut()
                _uiState.update {
                    it.copy(isLoading = false, error = "No se pudo crear el usuario")
                }
                return@launch
            }

            when (val profileResult = userRepository.createUserProfile(
                uid = uid,
                name = name,
                username = username,
                email = email
            )) {
                is Result.Failure -> {
                    authRepository.signOut()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = profileResult.message ?: "No se pudo guardar el perfil"
                        )
                    }
                    return@launch
                }

                is Result.Success -> Unit
            }

            authRepository.updateDisplayName(name)

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
