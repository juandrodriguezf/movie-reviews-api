package com.example.moviereviews.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.model.ArticleInfo
import com.example.moviereviews.data.repository.ArticleRepository
import com.example.moviereviews.data.repository.AuthRepository
import com.example.moviereviews.data.repository.ReviewRepository
import com.example.moviereviews.data.repository.UserRepository
import com.example.moviereviews.navigation.Screen
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
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val articleRepository: ArticleRepository,
    private val authRepositoryProvider: Provider<AuthRepository>,
    currentUserProvider: CurrentUserProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val currentUserId: String = currentUserProvider.currentUserId().orEmpty()

    private val userId: String =
        savedStateHandle[Screen.UserProfile.ARG_USER_ID] ?: currentUserId

    val isOwnProfile: Boolean = userId == currentUserId

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProfile(showLoading: Boolean = true) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = showLoading && it.user == null,
                    error = null,
                    reviewsError = null,
                    actionError = null
                )
            }

            when (val userResult = userRepository.getUser(userId)) {
                is Result.Failure -> _uiState.update {
                    if (it.user == null) {
                        it.copy(isLoading = false, error = userResult.message)
                    } else {
                        it.copy(isLoading = false, actionError = userResult.message)
                    }
                }

                is Result.Success -> {
                    val mainArticle = loadMainArticle(userResult.data.mainArticleId)

                    when (val reviewsResult = userRepository.getUserReviews(userId)) {
                        is Result.Success -> _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = userResult.data,
                                mainArticle = mainArticle,
                                reviews = reviewsResult.data,
                                error = null,
                                reviewsError = null
                            )
                        }

                        is Result.Failure -> _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = userResult.data,
                                mainArticle = mainArticle,
                                reviews = emptyList(),
                                error = null,
                                reviewsError = reviewsResult.message
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadMainArticle(articleId: String?): ArticleInfo? {
        if (articleId.isNullOrBlank()) {
            return null
        }

        return when (val result = articleRepository.getArticle(articleId)) {
            is Result.Success -> result.data
            is Result.Failure -> null
        }
    }

    fun logout() {
        if (BackendConfig.isFirestore) {
            authRepositoryProvider.get().signOut()
        }
    }

    fun deleteReview(reviewId: String) {
        if (_uiState.value.isDeleting) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, actionError = null) }

            when (val result = reviewRepository.deleteReview(reviewId)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isDeleting = false,
                        reviews = it.reviews.filterNot { review -> review.id == reviewId },
                        actionError = null
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(isDeleting = false, actionError = result.message)
                }
            }
        }
    }
}
