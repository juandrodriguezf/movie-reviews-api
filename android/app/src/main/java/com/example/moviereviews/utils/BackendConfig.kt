package com.example.moviereviews.utils

import com.example.moviereviews.BuildConfig

object BackendConfig {
    const val EXPRESS = "express"
    const val FIRESTORE = "firestore"

    private val backend = BuildConfig.BACKEND.lowercase()

    val isExpress: Boolean = backend == EXPRESS
    val isFirestore: Boolean = !isExpress
}
