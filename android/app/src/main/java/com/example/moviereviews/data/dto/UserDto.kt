package com.example.moviereviews.data.dto

import com.example.moviereviews.data.model.UserInfo

data class UserDto(
    val id: String,
    val username: String,
    val email: String? = null,
    val name: String,
    val avatarUrl: String,
    val bio: String = "",
    val mainArticleId: String? = null
)

fun UserDto.toUserInfo() = UserInfo(
    id = id,
    username = username,
    email = email,
    name = name,
    avatarUrl = avatarUrl,
    bio = bio,
    mainArticleId = mainArticleId
)
