package com.example.moviereviews.utils

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentUserProvider @Inject constructor(
    private val auth: FirebaseAuth
) {
    fun currentUserId(): String? = auth.currentUser?.uid
}
