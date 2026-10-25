package com.example.moviereviews.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviereviews.data.repository.ReviewRepository
import com.example.moviereviews.data.repository.UserRepository
import com.example.moviereviews.navigation.Screen
import com.example.moviereviews.utils.Constants
import com.example.moviereviews.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId: Int =
        savedStateHandle[Screen.UserProfile.ARG_USER_ID] ?: Constants.CURRENT_USER_ID

    val isOwnProfile: Boolean = userId == Constants.CURRENT_USER_ID

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProfile(showLoading: Boolean = true) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = showLoading && it.user == null,
                    error = null,
                    reviewsError = null,
                    actionError = null
                )
            }

            when (val userResult = userRepository.getUser(userId)) {
                is Result.Failure -> _uiState.update {
                    if (it.user == null) {
                        it.copy(isLoading = false, error = userResult.message)
                    } else {
                        it.copy(isLoading = false, actionError = userResult.message)
                    }
                }

                is Result.Success -> {
                    when (val reviewsResult = userRepository.getUserReviews(userId)) {
                        is Result.Success -> _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = userResult.data,
                                reviews = reviewsResult.data,
                                error = null,
                                reviewsError = null
                            )
                        }

                        is Result.Failure -> _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = userResult.data,
                                reviews = emptyList(),
                                error = null,
                                reviewsError = reviewsResult.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteReview(reviewId: Int) {
        if (_uiState.value.isDeleting) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, actionError = null) }

            when (val result = reviewRepository.deleteReview(reviewId)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isDeleting = false,
                        reviews = it.reviews.filterNot { review -> review.id == reviewId },
                        actionError = null
                    )
                }

                is Result.Failure -> _uiState.update {
                    it.copy(isDeleting = false, actionError = result.message)
                }
            }
        }
    }
}
