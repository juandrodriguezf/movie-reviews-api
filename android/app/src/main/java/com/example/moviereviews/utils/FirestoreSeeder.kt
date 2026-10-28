package com.example.moviereviews.utils

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreSeeder @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun seed() {
        val users = seedUsers()
        val articles = seedArticles()
        val reviews = seedReviews()
        val batch = firestore.batch()
        val timestamp = FieldValue.serverTimestamp()

        users.forEach { user ->
            batch.set(
                firestore.collection("users").document(user.id),
                mapOf(
                    "id" to user.id,
                    "username" to user.username,
                    "email" to user.email,
                    "name" to user.name,
                    "bio" to user.bio,
                    "avatarUrl" to user.avatarUrl,
                    "mainArticleId" to user.mainArticleId,
                    "createdAt" to timestamp,
                    "updatedAt" to timestamp
                )
            )
        }

        articles.forEach { article ->
            batch.set(
                firestore.collection("articles").document(article.id),
                mapOf(
                    "id" to article.id,
                    "title" to article.title,
                    "director" to article.director,
                    "genre" to article.genre,
                    "releaseYear" to article.releaseYear,
                    "synopsis" to article.synopsis,
                    "duration" to article.duration,
                    "score" to article.score,
                    "imageUrl" to article.imageUrl
                )
            )
        }

        reviews.forEach { review ->
            val user = users.first { it.id == review.userId }
            val article = articles.first { it.id == review.articleId }

            batch.set(
                firestore.collection("reviews").document(review.id),
                mapOf(
                    "id" to review.id,
                    "rating" to review.rating,
                    "comment" to review.comment,
                    "userId" to review.userId,
                    "articleId" to review.articleId,
                    "createdAt" to timestamp,
                    "updatedAt" to timestamp,
                    "user" to mapOf(
                        "id" to user.id,
                        "name" to user.name,
                        "username" to user.username,
                        "avatarUrl" to user.avatarUrl
                    ),
                    "article" to mapOf(
                        "id" to article.id,
                        "title" to article.title,
                        "imageUrl" to article.imageUrl
                    )
                )
            )
        }

        batch.commit().await()
    }

    private fun seedUsers() = listOf(
        SeedUser(
            id = "seed-camilo",
            username = "camilo.cine",
            email = "camilo.rojas@javeriana.edu.co",
            name = "Camilo Rojas",
            bio = "Amante del cine clasico y de los guiones bien contados.",
            avatarUrl = "https://i.pravatar.cc/150?img=12",
            mainArticleId = "2"
        ),
        SeedUser(
            id = "seed-valeria",
            username = "valeria_f",
            email = "valeria.fernandez@javeriana.edu.co",
            name = "Valeria Fernandez",
            bio = "Ciencia ficcion, fantasias y bandas sonoras memorables.",
            avatarUrl = "https://i.pravatar.cc/150?img=32",
            mainArticleId = "3"
        ),
        SeedUser(
            id = "seed-diego",
            username = "diego_rz",
            email = "diego.ramirez@javeriana.edu.co",
            name = "Diego Ramirez",
            bio = "Prefiero las historias crudas y los finales inesperados.",
            avatarUrl = "https://i.pravatar.cc/150?img=15",
            mainArticleId = "5"
        ),
        SeedUser(
            id = "seed-sara",
            username = "sara.mendez",
            email = "sara.mendez@javeriana.edu.co",
            name = "Sara Mendez",
            bio = "Drama, fantasia y cine latinoamericano.",
            avatarUrl = "https://i.pravatar.cc/150?img=45",
            mainArticleId = "6"
        )
    )

    private fun seedArticles() = listOf(
        SeedArticle(
            id = "1",
            title = "El viaje de Chihiro",
            director = "Hayao Miyazaki",
            genre = "Animacion",
            releaseYear = 2001,
            synopsis = "Una nina entra a un mundo de espiritus y debe trabajar en unos banos publicos para salvar a sus padres.",
            duration = 125,
            score = 9.3,
            imageUrl = "https://picsum.photos/seed/chihiro/500/750"
        ),
        SeedArticle(
            id = "2",
            title = "El padrino",
            director = "Francis Ford Coppola",
            genre = "Drama",
            releaseYear = 1972,
            synopsis = "El patriarca de una familia de la mafia intenta transferir el control de su imperio a su hijo menor.",
            duration = 175,
            score = 9.2,
            imageUrl = "https://picsum.photos/seed/padrino/500/750"
        ),
        SeedArticle(
            id = "3",
            title = "Interestelar",
            director = "Christopher Nolan",
            genre = "Ciencia ficcion",
            releaseYear = 2014,
            synopsis = "Un grupo de exploradores viaja a traves de un agujero de gusano para encontrar un nuevo hogar para la humanidad.",
            duration = 169,
            score = 8.7,
            imageUrl = "https://picsum.photos/seed/interestelar/500/750"
        ),
        SeedArticle(
            id = "4",
            title = "Pulp Fiction",
            director = "Quentin Tarantino",
            genre = "Crimen",
            releaseYear = 1994,
            synopsis = "Historias entrelazadas de dos sicarios, un boxeador y la esposa de un jefe criminal.",
            duration = 154,
            score = 8.9,
            imageUrl = "https://picsum.photos/seed/pulpfiction/500/750"
        ),
        SeedArticle(
            id = "5",
            title = "Ciudad de Dios",
            director = "Fernando Meirelles",
            genre = "Drama",
            releaseYear = 2002,
            synopsis = "La vida de dos jovenes en una favela de Rio de Janeiro toma caminos opuestos entre el crimen y la fotografia.",
            duration = 130,
            score = 8.6,
            imageUrl = "https://picsum.photos/seed/ciudaddedios/500/750"
        ),
        SeedArticle(
            id = "6",
            title = "El laberinto del fauno",
            director = "Guillermo del Toro",
            genre = "Fantasia",
            releaseYear = 2006,
            synopsis = "Una nina descubre un laberinto magico en la Espana franquista y recibe tres pruebas de un fauno.",
            duration = 118,
            score = 8.2,
            imageUrl = "https://picsum.photos/seed/fauno/500/750"
        ),
        SeedArticle(
            id = "7",
            title = "Volver",
            director = "Pedro Almodovar",
            genre = "Drama",
            releaseYear = 2006,
            synopsis = "Tres generaciones de mujeres de La Mancha enfrentan la muerte, los secretos y la supervivencia.",
            duration = 121,
            score = 7.6,
            imageUrl = "https://picsum.photos/seed/volver/500/750"
        ),
        SeedArticle(
            id = "8",
            title = "Relatos salvajes",
            director = "Damian Szifron",
            genre = "Comedia negra",
            releaseYear = 2014,
            synopsis = "Seis historias sobre la venganza y la desesperacion de personajes al borde del colapso.",
            duration = 122,
            score = 8.1,
            imageUrl = "https://picsum.photos/seed/relatos/500/750"
        )
    )

    private fun seedReviews() = listOf(
        SeedReview(
            id = "review-1",
            userId = "seed-camilo",
            articleId = "1",
            rating = 5,
            comment = "Una obra maestra de la animacion, cada plano vale la pena."
        ),
        SeedReview(
            id = "review-2",
            userId = "seed-valeria",
            articleId = "2",
            rating = 5,
            comment = "El guion y las actuaciones siguen siendo impecables."
        ),
        SeedReview(
            id = "review-3",
            userId = "seed-diego",
            articleId = "3",
            rating = 4,
            comment = "La parte cientifica es discutible, pero la experiencia es enorme."
        ),
        SeedReview(
            id = "review-4",
            userId = "seed-sara",
            articleId = "4",
            rating = 5,
            comment = "La estructura narrativa no envejece ni un poco."
        ),
        SeedReview(
            id = "review-5",
            userId = "seed-camilo",
            articleId = "5",
            rating = 4,
            comment = "Cruda y potente, con un ritmo que no da respiro."
        ),
        SeedReview(
            id = "review-6",
            userId = "seed-valeria",
            articleId = "6",
            rating = 5,
            comment = "Fantasia y guerra mezcladas con una sensibilidad unica."
        ),
        SeedReview(
            id = "review-7",
            userId = "seed-diego",
            articleId = "7",
            rating = 4,
            comment = "Los personajes femeninos sostienen toda la pelicula."
        ),
        SeedReview(
            id = "review-8",
            userId = "seed-sara",
            articleId = "8",
            rating = 3,
            comment = "Divertida por momentos, aunque irregular como antologia."
        )
    )
}

private data class SeedUser(
    val id: String,
    val username: String,
    val email: String,
    val name: String,
    val bio: String,
    val avatarUrl: String,
    val mainArticleId: String
)

private data class SeedArticle(
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

private data class SeedReview(
    val id: String,
    val userId: String,
    val articleId: String,
    val rating: Int,
    val comment: String
)
