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
    suspend fun createReview(
        userId: Int,
        articleId: Int,
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
        id: Int,
        userId: Int,
        articleId: Int,
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

    suspend fun deleteReview(id: Int): Result<Unit> = apiCall {
        remoteDataSource.deleteReview(id)
    }
}
