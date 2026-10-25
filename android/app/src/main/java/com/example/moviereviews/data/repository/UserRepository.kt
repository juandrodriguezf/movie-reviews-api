package com.example.moviereviews.data.repository

import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.dto.toReviewInfo
import com.example.moviereviews.data.dto.toUserInfo
import com.example.moviereviews.data.model.ReviewInfo
import com.example.moviereviews.data.model.UserInfo
import com.example.moviereviews.utils.Result
import com.example.moviereviews.utils.apiCall
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) {
    suspend fun getUser(id: Int): Result<UserInfo> = apiCall {
        remoteDataSource.getUser(id).toUserInfo()
    }

    suspend fun getUserReviews(id: Int): Result<List<ReviewInfo>> = apiCall {
        remoteDataSource.getUserReviews(id).map { it.toReviewInfo() }
    }
}
