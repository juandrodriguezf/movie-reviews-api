package com.example.moviereviews.data.dto

import com.example.moviereviews.data.model.ArticleInfo

data class ArticleDto(
    val id: Int,
    val title: String,
    val director: String,
    val genre: String,
    val releaseYear: Int,
    val synopsis: String,
    val duration: Int,
    val score: Double,
    val imageUrl: String
)

fun ArticleDto.toArticleInfo() = ArticleInfo(
    id = id,
    title = title,
    director = director,
    genre = genre,
    releaseYear = releaseYear,
    synopsis = synopsis,
    duration = duration,
    score = score,
    imageUrl = imageUrl
)
