package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.ArticleRemoteDataSource
import com.example.moviereviews.data.datasource.services.MovieReviewsService
import javax.inject.Inject

class RetrofitArticleDataSource @Inject constructor(
    private val service: MovieReviewsService
) : ArticleRemoteDataSource {
    override suspend fun getArticles() = service.getArticles()

    override suspend fun getArticle(id: String) = service.getArticle(id)

    override suspend fun getArticleReviews(id: String) = service.getArticleReviews(id)
}
