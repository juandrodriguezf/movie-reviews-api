package com.example.moviereviews.data.dto

import com.example.moviereviews.data.model.ArticleSummaryInfo

data class ArticleSummaryDto(
    val id: String,
    val title: String,
    val imageUrl: String
)

fun ArticleSummaryDto.toArticleSummaryInfo() = ArticleSummaryInfo(
    id = id,
    title = title,
    imageUrl = imageUrl
)
