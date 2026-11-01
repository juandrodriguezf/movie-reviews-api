package com.example.moviereviews.screens.review

import com.example.moviereviews.data.model.ArticleInfo

data class ReviewFormUiState(
    val isLoading: Boolean = true,
    val isEditing: Boolean = false,
    val isReady: Boolean = false,
    val article: ArticleInfo? = null,
    val rating: Int = 5,
    val comment: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)
