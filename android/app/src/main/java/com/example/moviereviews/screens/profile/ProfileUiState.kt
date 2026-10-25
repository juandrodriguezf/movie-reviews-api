package com.example.moviereviews.screens.profile

import com.example.moviereviews.data.model.ReviewInfo
import com.example.moviereviews.data.model.UserInfo

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: UserInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val error: String? = null,
    val reviewsError: String? = null
)
