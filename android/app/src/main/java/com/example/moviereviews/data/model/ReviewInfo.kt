package com.example.moviereviews.data.model

data class ReviewInfo(
    val id: String,
    val rating: Int,
    val comment: String,
    val userId: String,
    val articleId: String,
    val createdAt: String,
    val user: UserInfo?,
    val article: ArticleSummaryInfo?
)
