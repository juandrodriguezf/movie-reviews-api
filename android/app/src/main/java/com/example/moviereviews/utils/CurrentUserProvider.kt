package com.example.moviereviews.utils

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class CurrentUserProvider @Inject constructor(
    private val authProvider: Provider<FirebaseAuth>
) {
    fun currentUserId(): String? =
        if (BackendConfig.isExpress) {
            Constants.EXPRESS_USER_ID
        } else {
            authProvider.get().currentUser?.uid
        }
}
