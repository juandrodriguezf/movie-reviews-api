package com.example.moviereviews.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.ArticleRepository
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = articleRepository.getArticles()) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        articles = result.data,
                        error = null
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }
    }
}
