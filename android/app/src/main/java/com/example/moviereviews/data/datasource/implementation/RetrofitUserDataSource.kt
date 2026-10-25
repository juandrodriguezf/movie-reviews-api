package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.datasource.services.MovieReviewsService
import javax.inject.Inject

class RetrofitUserDataSource @Inject constructor(
    private val service: MovieReviewsService
) : UserRemoteDataSource {
    override suspend fun getUser(id: Int) = service.getUser(id)

    override suspend fun getUserReviews(id: Int) = service.getUserReviews(id)
}
