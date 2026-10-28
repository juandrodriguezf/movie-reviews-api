package com.example.moviereviews.screens.article

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.moviereviews.data.model.ArticleInfo
import com.example.moviereviews.data.model.ReviewInfo
import java.util.Locale

private val StarColor = Color(0xFFF5A623)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    onBack: () -> Unit,
    onUserClick: (String) -> Unit,
    onWriteReview: (String) -> Unit,
    onEditReview: (String, String) -> Unit,
    viewModel: ArticleDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var reviewToDelete by remember { mutableStateOf<ReviewInfo?>(null) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadArticle(
            showLoading = viewModel.uiState.value.article == null
        )
    }

    reviewToDelete?.let { review ->
        AlertDialog(
            onDismissRequest = {
                if (!uiState.isDeleting) {
                    reviewToDelete = null
                }
            },
            title = { Text("Eliminar resena") },
            text = { Text("Esta accion no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    enabled = !uiState.isDeleting,
                    onClick = {
                        reviewToDelete = null
                        viewModel.deleteReview(review.id)
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !uiState.isDeleting,
                    onClick = { reviewToDelete = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val article = uiState.article

        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding))

            uiState.error != null -> ErrorState(
                message = uiState.error.orEmpty(),
                onRetry = viewModel::loadArticle,
                modifier = Modifier.padding(innerPadding)
            )

            article != null -> ArticleContent(
                article = article,
                currentUserId = uiState.currentUserId,
                reviews = uiState.reviews,
                reviewsError = uiState.reviewsError,
                actionError = uiState.actionError,
                onUserClick = onUserClick,
                onWriteReview = { onWriteReview(article.id) },
                onEditReview = { review -> onEditReview(article.id, review.id) },
                onDeleteReview = { reviewToDelete = it },
                modifier = Modifier.padding(innerPadding)
            )

            else -> EmptyState(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun ArticleContent(
    article: ArticleInfo,
    currentUserId: String,
    reviews: List<ReviewInfo>,
    reviewsError: String?,
    actionError: String?,
    onUserClick: (String) -> Unit,
    onWriteReview: () -> Unit,
    onEditReview: (ReviewInfo) -> Unit,
    onDeleteReview: (ReviewInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val ownReview = reviews.firstOrNull {
        it.userId == currentUserId
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${article.releaseYear} · ${article.duration} min · ${article.genre}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Dirigida por ${article.director}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    ScoreBadge(score = article.score)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = article.synopsis,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Resenas (${reviews.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (ownReview == null) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onWriteReview,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Escribir resena")
                    }
                }

                if (actionError != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = actionError,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (reviewsError != null) {
            item {
                Text(
                    text = reviewsError,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else if (reviews.isEmpty()) {
            item {
                Text(
                    text = "Todavia no hay resenas para esta pelicula.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else {
            items(reviews, key = { it.id }) { review ->
                ReviewCard(
                    review = review,
                    isOwn = ownReview?.id == review.id,
                    onUserClick = onUserClick,
                    onEdit = { onEditReview(review) },
                    onDelete = { onDeleteReview(review) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(
    review: ReviewInfo,
    isOwn: Boolean,
    onUserClick: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = review.user != null) {
                review.user?.let { onUserClick(it.id) }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            val user = review.user

            if (user != null) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = user.name,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = user?.name ?: "Usuario",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = ratingStars(review.rating),
                        style = MaterialTheme.typography.labelMedium,
                        color = StarColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodyMedium
                )

                if (isOwn) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onEdit) {
                            Text("Editar")
                        }

                        TextButton(onClick = onDelete) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreBadge(score: Double) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xCC1C1B1F),
        contentColor = Color.White
    ) {
        Text(
            text = "★ " + String.format(Locale.US, "%.1f", score),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private fun ratingStars(rating: Int): String {
    val filled = rating.coerceIn(0, 5)
    return "★".repeat(filled) + "☆".repeat(5 - filled)
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No se encontro la pelicula",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
