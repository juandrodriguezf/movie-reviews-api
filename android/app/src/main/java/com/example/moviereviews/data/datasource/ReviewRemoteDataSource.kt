package com.example.moviereviews.data.datasource

import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.ReviewRequestDto

interface ReviewRemoteDataSource {
    suspend fun createReview(request: ReviewRequestDto): ReviewDto
    suspend fun updateReview(id: Int, request: ReviewRequestDto): ReviewDto
    suspend fun deleteReview(id: Int)
}
