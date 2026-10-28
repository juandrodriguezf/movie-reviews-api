package com.example.moviereviews.data.datasource.services

import com.example.moviereviews.data.dto.ArticleDto
import com.example.moviereviews.data.dto.MessageDto
import com.example.moviereviews.data.dto.RetrofitReviewRequestDto
import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MovieReviewsService {
    @GET("articles")
    suspend fun getArticles(): List<ArticleDto>

    @GET("articles/{id}")
    suspend fun getArticle(@Path("id") id: String): ArticleDto

    @GET("articles/{id}/reviews")
    suspend fun getArticleReviews(@Path("id") id: String): List<ReviewDto>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    @GET("users/{id}/reviews")
    suspend fun getUserReviews(@Path("id") id: String): List<ReviewDto>

    @GET("reviews/{id}")
    suspend fun getReview(@Path("id") id: String): ReviewDto

    @POST("reviews")
    suspend fun createReview(@Body request: RetrofitReviewRequestDto): ReviewDto

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: String,
        @Body request: RetrofitReviewRequestDto
    ): ReviewDto

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String): MessageDto
}
