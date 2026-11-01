package com.example.moviereviews.data.datasource.implementation

import com.example.moviereviews.data.datasource.ArticleRemoteDataSource
import com.example.moviereviews.data.dto.ArticleDto
import com.example.moviereviews.data.dto.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreArticleDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : ArticleRemoteDataSource {
    override suspend fun getArticles(): List<ArticleDto> =
        firestore.collection("articles")
            .get()
            .await()
            .documents
            .map { it.toArticleDto() }
            .sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }

    override suspend fun getArticle(id: String): ArticleDto {
        val snapshot = firestore.collection("articles").document(id).get().await()

        if (!snapshot.exists()) {
            throw Exception("Articulo no encontrado")
        }

        return snapshot.toArticleDto()
    }

    override suspend fun getArticleReviews(id: String): List<ReviewDto> =
        firestore.collection("reviews")
            .whereEqualTo("articleId", id)
            .get()
            .await()
            .documents
            .map { it.toReviewDto() }
            .sortedByDescending { it.createdAt.orEmpty() }
}
