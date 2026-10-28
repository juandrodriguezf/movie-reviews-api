package com.example.moviereviews.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.ArticleRepository
import com.example.moviereviews.utils.FirestoreSeeder
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val firestoreSeeder: FirestoreSeeder
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

    fun seedDemoData() {
        if (_uiState.value.isSeeding) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSeeding = true, error = null) }

            try {
                firestoreSeeder.seed()
                _uiState.update { it.copy(isSeeding = false) }
                loadArticles()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSeeding = false,
                        error = e.message ?: "No se pudieron cargar los datos de ejemplo"
                    )
                }
            }
        }
    }
}
