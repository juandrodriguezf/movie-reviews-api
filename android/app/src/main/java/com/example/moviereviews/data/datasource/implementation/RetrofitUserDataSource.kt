package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.datasource.services.MovieReviewsService
import com.example.moviereviews.data.dto.UserDto
import javax.inject.Inject

class RetrofitUserDataSource @Inject constructor(
    private val service: MovieReviewsService
) : UserRemoteDataSource {
    override suspend fun getUser(id: String) = service.getUser(id)

    override suspend fun getUserReviews(id: String) = service.getUserReviews(id)

    override suspend fun usernameExists(username: String): Boolean =
        throw UnsupportedOperationException("usernameExists solo esta disponible con Firestore")

    override suspend fun createUserProfile(user: UserDto): UserDto =
        throw UnsupportedOperationException("createUserProfile solo esta disponible con Firestore")

    override suspend fun updateUserProfile(user: UserDto): UserDto =
        throw UnsupportedOperationException("updateUserProfile solo esta disponible con Firestore")
}
