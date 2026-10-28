package com.example.moviereviews.data.dto

import com.example.moviereviews.data.model.ReviewInfo

data class ReviewDto(
    val id: String,
    val rating: Int,
    val comment: String,
    val userId: String,
    val articleId: String,
    val createdAt: String? = null,
    val user: UserDto? = null,
    val article: ArticleSummaryDto? = null
)

fun ReviewDto.toReviewInfo() = ReviewInfo(
    id = id,
    rating = rating,
    comment = comment,
    userId = userId,
    articleId = articleId,
    createdAt = createdAt.orEmpty(),
    user = user?.toUserInfo(),
    article = article?.toArticleSummaryInfo()
)
