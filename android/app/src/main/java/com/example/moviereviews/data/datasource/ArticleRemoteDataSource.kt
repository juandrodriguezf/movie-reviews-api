package com.example.moviereviews.data.datasource

import com.example.moviereviews.data.dto.ArticleDto
import com.example.moviereviews.data.dto.ReviewDto

interface ArticleRemoteDataSource {
    suspend fun getArticles(): List<ArticleDto>
    suspend fun getArticle(id: String): ArticleDto
    suspend fun getArticleReviews(id: String): List<ReviewDto>
}
