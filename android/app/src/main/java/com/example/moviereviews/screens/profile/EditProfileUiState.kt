package com.example.moviereviews.screens.profile

import com.example.moviereviews.data.model.ArticleSummaryInfo

data class EditProfileUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val bio: String = "",
    val avatarUrl: String = "",
    val mainArticleId: String? = null,
    val articles: List<ArticleSummaryInfo> = emptyList(),
    val error: String? = null,
    val nameError: String? = null,
    val usernameError: String? = null
)
