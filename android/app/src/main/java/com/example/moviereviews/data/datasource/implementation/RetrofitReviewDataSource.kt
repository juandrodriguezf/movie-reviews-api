package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.ReviewRemoteDataSource
import com.example.moviereviews.data.datasource.services.MovieReviewsService
import com.example.moviereviews.data.dto.ReviewRequestDto
import javax.inject.Inject

class RetrofitReviewDataSource @Inject constructor(
    private val service: MovieReviewsService
) : ReviewRemoteDataSource {
    override suspend fun getReview(id: Int) = service.getReview(id)

    override suspend fun createReview(request: ReviewRequestDto) = service.createReview(request)

    override suspend fun updateReview(id: Int, request: ReviewRequestDto) =
        service.updateReview(id, request)

    override suspend fun deleteReview(id: Int) {
        service.deleteReview(id)
    }
}
