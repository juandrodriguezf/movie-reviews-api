package com.example.moviereviews.data.datasource

import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.UserDto

interface UserRemoteDataSource {
    suspend fun getUser(id: Int): UserDto
    suspend fun getUserReviews(id: Int): List<ReviewDto>
}
