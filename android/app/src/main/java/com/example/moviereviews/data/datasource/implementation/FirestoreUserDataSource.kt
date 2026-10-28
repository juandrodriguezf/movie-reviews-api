package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.UserRemoteDataSource
import com.example.moviereviews.data.dto.ReviewDto
import com.example.moviereviews.data.dto.UserDto
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
}
