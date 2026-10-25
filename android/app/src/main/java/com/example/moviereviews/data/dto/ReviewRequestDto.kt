package com.example.moviereviews.data.dto

data class ReviewRequestDto(
    val rating: Int,
    val comment: String,
    val userId: Int,
    val articleId: Int
)
