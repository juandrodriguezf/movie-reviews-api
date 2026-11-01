package com.example.moviereviews.data.dto

data class RetrofitReviewRequestDto(
    val rating: Int,
    val comment: String,
    val userId: Int,
    val articleId: Int
)
