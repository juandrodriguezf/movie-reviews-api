package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.dto.ArticleDto
import com.example.moviereviews.data.dto.ArticleSummaryDto
import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.UserDto
import com.google.firebase.firestore.DocumentSnapshot

internal fun DocumentSnapshot.toUserDto() = UserDto(
    id = id,
    username = getString("username").orEmpty(),
    email = getString("email"),
    name = getString("name").orEmpty(),
    avatarUrl = getString("avatarUrl").orEmpty(),
    bio = getString("bio").orEmpty(),
    mainArticleId = getString("mainArticleId")
)

internal fun DocumentSnapshot.toArticleDto() = ArticleDto(
    id = id,
    title = getString("title").orEmpty(),
    director = getString("director").orEmpty(),
    genre = getString("genre").orEmpty(),
    releaseYear = getLong("releaseYear")?.toInt() ?: 0,
    synopsis = getString("synopsis").orEmpty(),
    duration = getLong("duration")?.toInt() ?: 0,
    score = getDouble("score") ?: 0.0,
    imageUrl = getString("imageUrl").orEmpty()
)

internal fun DocumentSnapshot.toReviewDto(): ReviewDto {
    val userMap = get("user") as? Map<*, *>
    val articleMap = get("article") as? Map<*, *>

    return ReviewDto(
        id = id,
        rating = getLong("rating")?.toInt() ?: 0,
        comment = getString("comment").orEmpty(),
        userId = getString("userId").orEmpty(),
        articleId = getString("articleId").orEmpty(),
        createdAt = getTimestamp("createdAt")?.toDate()?.time?.toString(),
        user = userMap?.toUserDto(),
        article = articleMap?.toArticleSummaryDto()
    )
}

private fun Map<*, *>.toUserDto(): UserDto? {
    val userId = this["id"] as? String ?: return null

    return UserDto(
        id = userId,
        username = this["username"] as? String ?: "",
        email = this["email"] as? String,
        name = this["name"] as? String ?: "",
        avatarUrl = this["avatarUrl"] as? String ?: "",
        bio = this["bio"] as? String ?: "",
        mainArticleId = this["mainArticleId"] as? String
    )
}

private fun Map<*, *>.toArticleSummaryDto(): ArticleSummaryDto? {
    val articleId = this["id"] as? String ?: return null

    return ArticleSummaryDto(
        id = articleId,
        title = this["title"] as? String ?: "",
        imageUrl = this["imageUrl"] as? String ?: ""
    )
}
