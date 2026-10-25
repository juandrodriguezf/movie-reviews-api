package com.example.moviereviews.data.dto

import com.example.moviereviews.data.model.UserInfo

data class UserDto(
    val id: Int,
    val username: String,
    val email: String? = null,
    val name: String,
    val avatarUrl: String
)

fun UserDto.toUserInfo() = UserInfo(
    id = id,
    username = username,
    email = email,
    name = name,
    avatarUrl = avatarUrl
)
