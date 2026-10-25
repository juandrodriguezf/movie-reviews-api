package com.example.moviereviews.screens.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviereviews.data.model.ArticleInfo

private val StarColor = Color(0xFFF5A623)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewFormScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ReviewFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isEditing) "Editar resena" else "Escribir resena")
                },
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
        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding))

            !uiState.isReady -> ErrorState(
                message = uiState.error ?: "No se pudo cargar el formulario",
                onRetry = viewModel::loadForm,
                modifier = Modifier.padding(innerPadding)
            )

            else -> ReviewFormContent(
                uiState = uiState,
                onRatingChange = viewModel::onRatingChange,
                onCommentChange = viewModel::onCommentChange,
                onSave = viewModel::save,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ReviewFormContent(
    uiState: ReviewFormUiState,
    onRatingChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        val article = uiState.article

        if (article != null) {
            ArticleHeader(article)
            Spacer(modifier = Modifier.height(24.dp))
        }

        Text(
            text = "Tu calificacion",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        RatingSelector(
            rating = uiState.rating,
            enabled = !uiState.isSaving,
            onRatingChange = onRatingChange
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Comentario",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.comment,
            onValueChange = onCommentChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
            minLines = 5,
            maxLines = 8,
            placeholder = {
                Text("Que te parecio la pelicula?")
            },
            supportingText = {
                Text("${uiState.comment.length}/500")
            }
        )

        if (uiState.error != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSave,
            enabled = !uiState.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (uiState.isEditing) "Guardar cambios" else "Publicar resena")
            }
        }
    }
}

@Composable
private fun ArticleHeader(article: ArticleInfo) {
    Text(
        text = article.title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "${article.releaseYear} · ${article.director}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun RatingSelector(
    rating: Int,
    enabled: Boolean,
    onRatingChange: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        (1..5).forEach { value ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(enabled = enabled) {
                        onRatingChange(value)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (value <= rating) "★" else "☆",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (value <= rating) {
                        StarColor
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
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
