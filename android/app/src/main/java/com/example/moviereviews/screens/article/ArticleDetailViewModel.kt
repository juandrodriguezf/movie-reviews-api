package com.example.moviereviews.screens.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.ArticleRepository
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val articleId: Int =
        savedStateHandle[Screen.ArticleDetail.ARG_ARTICLE_ID] ?: -1

    private val _uiState = MutableStateFlow(ArticleDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadArticle()
    }

    fun loadArticle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, reviewsError = null) }

            if (articleId <= 0) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Articulo no encontrado")
                }
                return@launch
            }

            when (val articleResult = articleRepository.getArticle(articleId)) {
                is Result.Failure -> _uiState.update {
                    it.copy(isLoading = false, error = articleResult.message)
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
}
