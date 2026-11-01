package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.UserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreUserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {
    override suspend fun getUser(id: String): UserDto {
        val snapshot = firestore.collection("users").document(id).get().await()

        if (!snapshot.exists()) {
            throw Exception("Usuario no encontrado")
        }

        return snapshot.toUserDto()
    }

    override suspend fun getUserReviews(id: String): List<ReviewDto> =
        firestore.collection("reviews")
            .whereEqualTo("userId", id)
            .get()
            .await()
            .documents
            .map { it.toReviewDto() }
            .sortedByDescending { it.createdAt.orEmpty() }

    override suspend fun usernameExists(username: String): Boolean =
        firestore.collection("users")
            .whereEqualTo("username", username)
            .limit(1)
            .get()
            .await()
            .documents
            .isNotEmpty()

    override suspend fun createUserProfile(user: UserDto): UserDto {
        firestore.collection("users").document(user.id).set(
            mapOf(
                "id" to user.id,
                "username" to user.username,
                "email" to user.email,
                "name" to user.name,
                "avatarUrl" to user.avatarUrl,
                "bio" to user.bio,
                "mainArticleId" to user.mainArticleId,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
        ).await()

        return getUser(user.id)
    }

    override suspend fun updateUserProfile(user: UserDto): UserDto {
        val userRef = firestore.collection("users").document(user.id)
        val reviews = firestore.collection("reviews")
            .whereEqualTo("userId", user.id)
            .get()
            .await()

        val batch = firestore.batch()

        batch.update(
            userRef,
            mapOf(
                "username" to user.username,
                "name" to user.name,
                "avatarUrl" to user.avatarUrl,
                "bio" to user.bio,
                "mainArticleId" to user.mainArticleId,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        )

        val reviewUser = mapOf(
            "id" to user.id,
            "name" to user.name,
            "username" to user.username,
            "avatarUrl" to user.avatarUrl
        )

        reviews.documents.forEach { review ->
            batch.update(review.reference, "user", reviewUser)
        }

        batch.commit().await()

        return getUser(user.id)
    }
}
