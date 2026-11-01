package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.ReviewRemoteDataSource
import com.example.moviereviews.data.datasource.services.MovieReviewsService
import com.example.moviereviews.data.dto.RetrofitReviewRequestDto
import com.example.moviereviews.data.dto.ReviewRequestDto
import javax.inject.Inject

class RetrofitReviewDataSource @Inject constructor(
    private val service: MovieReviewsService
) : ReviewRemoteDataSource {
    override suspend fun getReview(id: String) = service.getReview(id)

    override suspend fun createReview(request: ReviewRequestDto) =
        service.createReview(request.toRetrofitRequest())

    override suspend fun updateReview(id: String, request: ReviewRequestDto) =
        service.updateReview(id, request.toRetrofitRequest())

    override suspend fun deleteReview(id: String) {
        service.deleteReview(id)
    }

    private fun ReviewRequestDto.toRetrofitRequest() = RetrofitReviewRequestDto(
        rating = rating,
        comment = comment,
        userId = userId.toIntOrNull()
            ?: throw IllegalArgumentException("userId invalido"),
        articleId = articleId.toIntOrNull()
            ?: throw IllegalArgumentException("articleId invalido")
    )
}
