package com.example.moviereviews.utils

import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
}

suspend fun <T> apiCall(block: suspend () -> T): Result<T> =
    try {
        Result.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        Result.Failure(e.message().ifBlank { "Error del servidor (${e.code()})" })
    } catch (e: Exception) {
        Result.Failure(e.message ?: "Error de conexion")
    }
