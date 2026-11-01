package com.example.moviereviews.data.model

data class ArticleInfo(
    val id: String,
    val title: String,
    val director: String,
    val genre: String,
    val releaseYear: Int,
    val synopsis: String,
    val duration: Int,
    val score: Double,
    val imageUrl: String
)
