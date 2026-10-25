package com.example.moviereviews.data.datasource

import com.example.moviereviews.data.dto.ArticleDto
import com.example.moviereviews.data.dto.ReviewDto

interface ArticleRemoteDataSource {
    suspend fun getArticles(): List<ArticleDto>
    suspend fun getArticle(id: Int): ArticleDto
    suspend fun getArticleReviews(id: Int): List<ReviewDto>
}
