package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.ReviewRemoteDataSource
import com.example.moviereviews.data.dto.ArticleDto
import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.ReviewRequestDto
import com.example.moviereviews.data.dto.UserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreReviewDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewRemoteDataSource {
    override suspend fun getReview(id: String): ReviewDto {
        val snapshot = firestore.collection("reviews").document(id).get().await()

        if (!snapshot.exists()) {
            throw Exception("Resena no encontrada")
        }

        return snapshot.toReviewDto()
    }

    override suspend fun createReview(request: ReviewRequestDto): ReviewDto {
        val user = getUser(request.userId)
        val article = getArticle(request.articleId)
        val reviewRef = firestore.collection("reviews").document()

        val review = mapOf(
            "id" to reviewRef.id,
            "rating" to request.rating,
            "comment" to request.comment,
            "userId" to request.userId,
            "articleId" to request.articleId,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp(),
            "user" to user.toReviewUserMap(),
            "article" to article.toReviewArticleMap()
        )

        reviewRef.set(review).await()

        return getReview(reviewRef.id)
    }

    override suspend fun updateReview(id: String, request: ReviewRequestDto): ReviewDto {
        val user = getUser(request.userId)
        val article = getArticle(request.articleId)
        val reviewRef = firestore.collection("reviews").document(id)
        val snapshot = reviewRef.get().await()

        if (!snapshot.exists()) {
            throw Exception("Resena no encontrada")
        }

        reviewRef.update(
            mapOf(
                "rating" to request.rating,
                "comment" to request.comment,
                "userId" to request.userId,
                "articleId" to request.articleId,
                "updatedAt" to FieldValue.serverTimestamp(),
                "user" to user.toReviewUserMap(),
                "article" to article.toReviewArticleMap()
            )
        ).await()

        return getReview(id)
    }

    override suspend fun deleteReview(id: String) {
        firestore.collection("reviews").document(id).delete().await()
    }

    private suspend fun getUser(id: String): UserDto {
        val snapshot = firestore.collection("users").document(id).get().await()

        if (!snapshot.exists()) {
            throw Exception("Usuario no encontrado")
        }

        return snapshot.toUserDto()
    }

    private suspend fun getArticle(id: String): ArticleDto {
        val snapshot = firestore.collection("articles").document(id).get().await()

        if (!snapshot.exists()) {
            throw Exception("Articulo no encontrado")
        }

        return snapshot.toArticleDto()
    }

    private fun UserDto.toReviewUserMap() = mapOf(
        "id" to id,
        "name" to name,
        "username" to username,
        "avatarUrl" to avatarUrl
    )

    private fun ArticleDto.toReviewArticleMap() = mapOf(
        "id" to id,
        "title" to title,
        "imageUrl" to imageUrl
    )
}
