package com.example.moviereviews.data.repository

import com.example.moviereviews.data.datasource.ReviewRemoteDataSource
import com.example.moviereviews.data.dto.ReviewRequestDto
import com.example.moviereviews.data.dto.toReviewInfo
import com.example.moviereviews.data.model.ReviewInfo
import com.example.moviereviews.utils.Result
import com.example.moviereviews.utils.apiCall
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val remoteDataSource: ReviewRemoteDataSource
) {
    suspend fun getReview(id: String): Result<ReviewInfo> = apiCall {
        remoteDataSource.getReview(id).toReviewInfo()
    }

    suspend fun createReview(
        userId: String,
        articleId: String,
        rating: Int,
        comment: String
    ): Result<ReviewInfo> = apiCall {
        remoteDataSource.createReview(
            ReviewRequestDto(
                rating = rating,
                comment = comment,
                userId = userId,
                articleId = articleId
            )
        ).toReviewInfo()
    }

    suspend fun updateReview(
        id: String,
        userId: String,
        articleId: String,
        rating: Int,
        comment: String
    ): Result<ReviewInfo> = apiCall {
        remoteDataSource.updateReview(
            id,
            ReviewRequestDto(
                rating = rating,
                comment = comment,
                userId = userId,
                articleId = articleId
            )
        ).toReviewInfo()
    }

    suspend fun deleteReview(id: String): Result<Unit> = apiCall {
        remoteDataSource.deleteReview(id)
    }
}
