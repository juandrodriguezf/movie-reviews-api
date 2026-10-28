package com.example.moviereviews.screens.profile

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.moviereviews.data.model.ArticleSummaryInfo
import com.example.moviereviews.data.model.ReviewInfo
import com.example.moviereviews.data.model.UserInfo

private val StarColor = Color(0xFFF5A623)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onArticleClick: (String) -> Unit,
    onEditReview: (String, String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var reviewToDelete by remember { mutableStateOf<ReviewInfo?>(null) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadProfile(
            showLoading = viewModel.uiState.value.user == null
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

    val title = if (viewModel.isOwnProfile) {
        "Mi perfil"
    } else {
        uiState.user?.name ?: "Perfil"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
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
        val user = uiState.user

        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding))

            uiState.error != null -> ErrorState(
                message = uiState.error.orEmpty(),
                onRetry = viewModel::loadProfile,
                modifier = Modifier.padding(innerPadding)
            )

            user != null -> ProfileContent(
                user = user,
                reviews = uiState.reviews,
                reviewsError = uiState.reviewsError,
                actionError = uiState.actionError,
                isOwnProfile = viewModel.isOwnProfile,
                onArticleClick = onArticleClick,
                onEditReview = onEditReview,
                onDeleteReview = { reviewToDelete = it },
                modifier = Modifier.padding(innerPadding)
            )

            else -> EmptyState(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun ProfileContent(
    user: UserInfo,
    reviews: List<ReviewInfo>,
    reviewsError: String?,
    actionError: String?,
    isOwnProfile: Boolean,
    onArticleClick: (String) -> Unit,
    onEditReview: (String, String) -> Unit,
    onDeleteReview: (ReviewInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            ProfileHeader(user = user, isOwnProfile = isOwnProfile)
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isOwnProfile) "Tus resenas" else "Sus resenas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (actionError != null) {
            item {
                Text(
                    text = actionError,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
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
                    text = "Todavia no hay resenas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else {
            items(reviews, key = { it.id }) { review ->
                ProfileReviewCard(
                    review = review,
                    isOwn = isOwnProfile,
                    onArticleClick = onArticleClick,
                    onEdit = {
                        review.article?.id?.let { articleId ->
                            onEditReview(articleId, review.id)
                        }
                    },
                    onDelete = { onDeleteReview(review) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    user: UserInfo,
    isOwnProfile: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user.avatarUrl.isNotBlank()) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = user.name,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "@${user.username}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (isOwnProfile && user.email != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileReviewCard(
    review: ReviewInfo,
    isOwn: Boolean,
    onArticleClick: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val article = review.article

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = article != null) {
                article?.let { onArticleClick(it.id) }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            if (article != null) {
                ArticleThumbnail(article)
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article?.title ?: "Pelicula",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = ratingStars(review.rating),
                    style = MaterialTheme.typography.labelMedium,
                    color = StarColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
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
private fun ArticleThumbnail(article: ArticleSummaryInfo) {
    AsyncImage(
        model = article.imageUrl,
        contentDescription = article.title,
        modifier = Modifier
            .size(width = 56.dp, height = 80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentScale = ContentScale.Crop
    )
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
            text = "No se encontro el usuario",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
