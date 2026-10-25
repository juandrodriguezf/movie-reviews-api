package com.example.moviereviews.screens.article

import com.example.moviereviews.data.model.ArticleInfo
import com.example.moviereviews.data.model.ReviewInfo

data class ArticleDetailUiState(
    val isLoading: Boolean = true,
    val article: ArticleInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val error: String? = null,
    val reviewsError: String? = null,
    val isDeleting: Boolean = false,
    val actionError: String? = null
)
