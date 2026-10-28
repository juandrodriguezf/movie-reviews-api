package com.example.moviereviews.data.repository

import com.example.moviereviews.data.datasource.AuthRemoteDataSource
import com.example.moviereviews.utils.Result
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.FirebaseNetworkException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) {
    val currentUserId: String?
        get() = remoteDataSource.currentUser?.uid

    suspend fun signIn(email: String, password: String): Result<Unit> = try {
        remoteDataSource.signIn(email, password)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Failure(authErrorMessage(e))
    }

    suspend fun signUp(email: String, password: String): Result<Unit> = try {
        remoteDataSource.signUp(email, password)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Failure(authErrorMessage(e))
    }

    suspend fun updateDisplayName(name: String): Result<Unit> = try {
        remoteDataSource.updateDisplayName(name)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Failure(e.message ?: "No se pudo actualizar el nombre")
    }

    fun signOut() {
        remoteDataSource.signOut()
    }

    private fun authErrorMessage(error: Exception): String = when (error) {
        is FirebaseAuthUserCollisionException -> "El correo ya esta en uso"
        is FirebaseAuthWeakPasswordException -> "La contrasena es muy debil"
        is FirebaseAuthInvalidCredentialsException -> "Credenciales incorrectas"
        is FirebaseAuthInvalidUserException -> "El usuario no existe"
        is FirebaseNetworkException -> "Error de conexion"
        else -> error.message ?: "Error de autenticacion"
    }
}
