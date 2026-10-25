package com.example.moviereviews.data.model

data class ReviewInfo(
    val id: Int,
    val rating: Int,
    val comment: String,
    val createdAt: String,
    val user: UserInfo?,
    val article: ArticleSummaryInfo?
)
