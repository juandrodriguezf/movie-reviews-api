package com.example.moviereviews.data.model

data class UserInfo(
    val id: String,
    val username: String,
    val email: String?,
    val name: String,
    val avatarUrl: String,
    val bio: String,
    val mainArticleId: String?
)
