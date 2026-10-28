package com.example.moviereviews.screens.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.ArticleRepository
import com.example.moviereviews.data.repository.ReviewRepository
import com.example.moviereviews.navigation.Screen
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val articleId: String =
        savedStateHandle[Screen.ArticleDetail.ARG_ARTICLE_ID] ?: ""

    private val _uiState = MutableStateFlow(ArticleDetailUiState())
    val uiState = _uiState.asStateFlow()

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

    fun loadArticle(showLoading: Boolean = true) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = showLoading && it.article == null,
                    error = null,
                    reviewsError = null,
                    actionError = null
                )
            }

            if (articleId.isBlank()) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Articulo no encontrado")
                }
                return@launch
            }

            when (val articleResult = articleRepository.getArticle(articleId)) {
                is Result.Failure -> _uiState.update {
                    if (it.article == null) {
                        it.copy(isLoading = false, error = articleResult.message)
                    } else {
                        it.copy(isLoading = false, actionError = articleResult.message)
                    }
                }

                is Result.Success -> {
                    when (val reviewsResult = articleRepository.getArticleReviews(articleId)) {
                        is Result.Success -> _uiState.update {
                            it.copy(
                                isLoading = false,
                                article = articleResult.data,
                                reviews = reviewsResult.data,
                                error = null,
                                reviewsError = null
                            )
                        }

                        is Result.Failure -> _uiState.update {
                            it.copy(
                                isLoading = false,
                                article = articleResult.data,
                                reviews = if (it.reviews.isEmpty()) {
                                    emptyList()
                                } else {
                                    it.reviews
                                },
                                error = null,
                                reviewsError = reviewsResult.message
                            )
                        }
                    }
                }
            }
        }
    }
}
