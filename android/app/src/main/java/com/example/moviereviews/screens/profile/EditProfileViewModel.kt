package com.example.moviereviews.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.model.ArticleSummaryInfo
import com.example.moviereviews.data.repository.ArticleRepository
import com.example.moviereviews.data.repository.AuthRepository
import com.example.moviereviews.data.repository.UserRepository
import com.example.moviereviews.utils.BackendConfig
import com.example.moviereviews.utils.CurrentUserProvider
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val authRepositoryProvider: Provider<AuthRepository>,
    currentUserProvider: CurrentUserProvider
) : ViewModel() {
    private val currentUserId: String? = currentUserProvider.currentUserId()
    private var originalUsername: String = ""

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val uid = currentUserId

        if (uid.isNullOrBlank()) {
            _uiState.update {
                it.copy(isLoading = false, error = "Usuario no autenticado")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val userResult = userRepository.getUser(uid)) {
                is Result.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = userResult.message)
                }

                is Result.Success -> {
                    val user = userResult.data
                    val articles = when (val articlesResult = articleRepository.getArticles()) {
                        is Result.Success -> articlesResult.data.map {
                            ArticleSummaryInfo(
                                id = it.id,
                                title = it.title,
                                imageUrl = it.imageUrl
                            )
                        }

                        is Result.Failure -> emptyList()
                    }

                    originalUsername = user.username

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            name = user.name,
                            username = user.username,
                            email = user.email.orEmpty(),
                            bio = user.bio,
                            avatarUrl = user.avatarUrl,
                            mainArticleId = user.mainArticleId,
                            articles = articles,
                            error = null
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, nameError = null, error = null) }
    }

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username, usernameError = null, error = null) }
    }

    fun onBioChange(bio: String) {
        _uiState.update { it.copy(bio = bio.take(200), error = null) }
    }

    fun onAvatarUrlChange(avatarUrl: String) {
        _uiState.update { it.copy(avatarUrl = avatarUrl, error = null) }
    }

    fun onMainArticleChange(articleId: String?) {
        _uiState.update { it.copy(mainArticleId = articleId, error = null) }
    }

    fun save() {
        val state = _uiState.value

        if (!BackendConfig.isFirestore) {
            _uiState.update {
                it.copy(error = "La edicion de perfil solo esta disponible con Firestore")
            }
            return
        }

        if (state.isLoading || state.isSaving || state.isSaved) {
            return
        }

        val uid = currentUserId
        if (uid.isNullOrBlank()) {
            _uiState.update { it.copy(error = "Usuario no autenticado") }
            return
        }

        val name = state.name.trim()
        val username = state.username.trim().lowercase()
        val bio = state.bio.trim()
        val avatarUrl = state.avatarUrl.trim()

        val nameError = if (name.isBlank()) "Ingresa tu nombre" else null
        val usernameError = when {
            username.isBlank() -> "Ingresa un nombre de usuario"
            username.length < 3 -> "Minimo 3 caracteres"
            else -> null
        }

        if (nameError != null || usernameError != null) {
            _uiState.update {
                it.copy(nameError = nameError, usernameError = usernameError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, error = null, nameError = null, usernameError = null)
            }

            if (username != originalUsername) {
                when (val usernameResult = userRepository.usernameExists(username)) {
                    is Result.Failure -> {
                        _uiState.update {
                            it.copy(isSaving = false, error = usernameResult.message)
                        }
                        return@launch
                    }

                    is Result.Success -> {
                        if (usernameResult.data) {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    usernameError = "Ese nombre de usuario ya esta en uso"
                                )
                            }
                            return@launch
                        }
                    }
                }
            }

            when (val result = userRepository.updateUserProfile(
                uid = uid,
                name = name,
                username = username,
                email = state.email,
                bio = bio,
                avatarUrl = avatarUrl,
                mainArticleId = state.mainArticleId
            )) {
                is Result.Failure -> _uiState.update {
                    it.copy(isSaving = false, error = result.message)
                }

                is Result.Success -> {
                    authRepositoryProvider.get().updateDisplayName(name)
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                }
            }
        }
    }
}
