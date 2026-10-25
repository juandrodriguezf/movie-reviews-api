package com.example.moviereviews.data.repository

import com.example.moviereviews.data.datasource.ArticleRemoteDataSource
import com.example.moviereviews.data.dto.toArticleInfo
import com.example.moviereviews.data.dto.toReviewInfo
import com.example.moviereviews.data.model.ArticleInfo
import com.example.moviereviews.data.model.ReviewInfo
import com.example.moviereviews.utils.Result
import com.example.moviereviews.utils.apiCall
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val remoteDataSource: ArticleRemoteDataSource
) {
    suspend fun getArticles(): Result<List<ArticleInfo>> = apiCall {
        remoteDataSource.getArticles().map { it.toArticleInfo() }
    }

    suspend fun getArticle(id: Int): Result<ArticleInfo> = apiCall {
        remoteDataSource.getArticle(id).toArticleInfo()
    }

    suspend fun getArticleReviews(id: Int): Result<List<ReviewInfo>> = apiCall {
        remoteDataSource.getArticleReviews(id).map { it.toReviewInfo() }
    }
}
