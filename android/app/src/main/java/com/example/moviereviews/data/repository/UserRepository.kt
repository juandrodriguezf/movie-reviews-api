package com.example.moviereviews.data.repository

import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.dto.UserDto
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
    suspend fun getUser(id: String): Result<UserInfo> = apiCall {
        remoteDataSource.getUser(id).toUserInfo()
    }

    suspend fun getUserReviews(id: String): Result<List<ReviewInfo>> = apiCall {
        remoteDataSource.getUserReviews(id).map { it.toReviewInfo() }
    }

    suspend fun usernameExists(username: String): Result<Boolean> = apiCall {
        remoteDataSource.usernameExists(username)
    }

    suspend fun createUserProfile(
        uid: String,
        name: String,
        username: String,
        email: String,
        avatarUrl: String = "",
        bio: String = "",
        mainArticleId: String? = "1"
    ): Result<UserInfo> = apiCall {
        remoteDataSource.createUserProfile(
            UserDto(
                id = uid,
                username = username,
                email = email,
                name = name,
                avatarUrl = avatarUrl,
                bio = bio,
                mainArticleId = mainArticleId
            )
        ).toUserInfo()
    }

    suspend fun updateUserProfile(
        uid: String,
        name: String,
        username: String,
        email: String,
        bio: String = "",
        avatarUrl: String = "",
        mainArticleId: String? = null
    ): Result<UserInfo> = apiCall {
        remoteDataSource.updateUserProfile(
            UserDto(
                id = uid,
                username = username,
                email = email,
                name = name,
                avatarUrl = avatarUrl,
                bio = bio,
                mainArticleId = mainArticleId
            )
        ).toUserInfo()
    }
}
