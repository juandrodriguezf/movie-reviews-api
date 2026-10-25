package com.example.moviereviews.data.dto

import com.example.moviereviews.data.model.ReviewInfo

data class ReviewDto(
    val id: Int,
    val rating: Int,
    val comment: String,
    val userId: Int,
    val articleId: Int,
    val createdAt: String? = null,
    val user: UserDto? = null,
    val article: ArticleSummaryDto? = null
)

fun ReviewDto.toReviewInfo() = ReviewInfo(
    id = id,
    rating = rating,
    comment = comment,
    createdAt = createdAt.orEmpty(),
    user = user?.toUserInfo(),
    article = article?.toArticleSummaryInfo()
)
