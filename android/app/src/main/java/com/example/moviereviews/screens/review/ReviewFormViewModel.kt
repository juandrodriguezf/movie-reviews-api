package com.example.moviereviews.screens.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.model.ArticleInfo
import com.example.moviereviews.data.repository.ArticleRepository
import com.example.moviereviews.data.repository.ReviewRepository
import com.example.moviereviews.navigation.Screen
import com.example.moviereviews.utils.Constants
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReviewFormViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val articleId: String =
        savedStateHandle[Screen.ReviewForm.ARG_ARTICLE_ID] ?: ""

    private val reviewId: String =
        savedStateHandle[Screen.ReviewForm.ARG_REVIEW_ID] ?: Constants.NO_REVIEW_ID

    private val isEditing: Boolean = reviewId.isNotBlank()

    private val _uiState = MutableStateFlow(
        ReviewFormUiState(isEditing = isEditing)
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadForm()
    }

    fun loadForm() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, isReady = false, error = null)
            }

            if (articleId.isBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Articulo no encontrado"
                    )
                }
                return@launch
            }

            when (val articleResult = articleRepository.getArticle(articleId)) {
                is Result.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = articleResult.message)
                }

                is Result.Success -> {
                    val article = articleResult.data

                    if (!isEditing) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isReady = true,
                                article = article,
                                rating = 5,
                                comment = "",
                                error = null
                            )
                        }
                        return@launch
                    }

                    loadReview(article)
                }
            }
        }
    }

    private suspend fun loadReview(article: ArticleInfo) {
        when (val reviewResult = reviewRepository.getReview(reviewId)) {
            is Result.Failure -> _uiState.update {
                it.copy(
                    isLoading = false,
                    article = article,
                    error = reviewResult.message
                )
            }

            is Result.Success -> {
                val review = reviewResult.data

                if (review.userId != Constants.CURRENT_USER_ID) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            article = article,
                            error = "No puedes editar esta resena"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isReady = true,
                            article = article,
                            rating = review.rating.coerceIn(1, 5),
                            comment = review.comment,
                            error = null
                        )
                    }
                }
            }
        }
    }

    fun onRatingChange(rating: Int) {
        _uiState.update {
            it.copy(rating = rating.coerceIn(1, 5), error = null)
        }
    }

    fun onCommentChange(comment: String) {
        _uiState.update {
            it.copy(comment = comment.take(500), error = null)
        }
    }

    fun save() {
        val current = _uiState.value
        val article = current.article

        if (current.isSaving || current.isSaved) {
            return
        }

        if (article == null) {
            _uiState.update { it.copy(error = "Articulo no encontrado") }
            return
        }

        if (current.rating !in 1..5) {
            _uiState.update { it.copy(error = "Selecciona una calificacion") }
            return
        }

        val comment = current.comment.trim()

        if (comment.isBlank()) {
            _uiState.update { it.copy(error = "Escribe un comentario") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val result = if (isEditing) {
                reviewRepository.updateReview(
                    id = reviewId,
                    userId = Constants.CURRENT_USER_ID,
                    articleId = article.id,
                    rating = current.rating,
                    comment = comment
                )
            } else {
                reviewRepository.createReview(
                    userId = Constants.CURRENT_USER_ID,
                    articleId = article.id,
                    rating = current.rating,
                    comment = comment
                )
            }

            when (result) {
                is Result.Success -> _uiState.update {
                    it.copy(isSaving = false, isSaved = true)
                }

                is Result.Failure -> _uiState.update {
                    it.copy(isSaving = false, error = result.message)
                }
            }
        }
    }
}
