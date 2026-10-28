package com.example.moviereviews.screens.home

import com.example.moviereviews.data.model.ArticleInfo

data class HomeUiState(
    val isLoading: Boolean = true,
    val articles: List<ArticleInfo> = emptyList(),
    val error: String? = null,
    val isSeeding: Boolean = false
)
