package com.ua.pavliyk.pager.data.common

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException

internal sealed class Result<out T>  {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val errorMessage: String, val code: Int? = null) : Result<Nothing>()
}

internal inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

internal inline fun <T> Result<T>.onFailure(action: (String) -> Unit): Result<T> {
    if (this is Result.Failure) {
        action(errorMessage)
    }
    return this
}

internal inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Failure -> this
    }
}

internal suspend inline fun <reified T> HttpResponse.handleResponse(): Result<T> {
    return when (status.value) {
        in 200..299 -> {
            try {
                val result = if (T::class == Unit::class) Unit as T else body<T>()
                Result.Success(result)
            } catch (e: SerializationException) {
                Result.Failure("Serialization error: ${e.message}")
            }
        }
        else -> {
            val errorBody = bodyAsText()
            Result.Failure("Error ${status.value}: $errorBody", status.value)
        }
    }
}

internal suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse
): Result<T> {
    return try {
        val response = block()
        response.handleResponse<T>()
    } catch (e: Exception) {
        Result.Failure("Network error: ${e.message}")
    }
}
