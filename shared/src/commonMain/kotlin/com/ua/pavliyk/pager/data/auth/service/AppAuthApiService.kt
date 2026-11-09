package com.ua.pavliyk.pager.data.auth.service

import com.ua.pavliyk.pager.data.auth.model.request.LoginRequest
import com.ua.pavliyk.pager.data.auth.model.request.RefreshRequest
import com.ua.pavliyk.pager.data.auth.model.response.LoginResponse
import com.ua.pavliyk.pager.data.auth.model.response.RefreshResponse
import com.ua.pavliyk.pager.data.auth.model.response.UserResponse
import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.common.safeRequest
import com.ua.pavliyk.pager.data.posts.service.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.plugins.auth.AuthCircuitBreaker

internal class AppAuthApiService(
    private val client: HttpClient,
) : AuthApiService {

    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        return client.safeRequest {
            post("$BASE_URL$AUTH$LOGIN") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
        }
    }

    override suspend fun refresh(refreshToken: String): Result<RefreshResponse> {
        return client.safeRequest {
            post("$BASE_URL$AUTH$REFRESH") {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequest(refreshToken))
                attributes.put(AuthCircuitBreaker, Unit)
            }
        }
    }

    override suspend fun getCurrentUser(): Result<UserResponse> {
        return client.safeRequest {
            get("$BASE_URL$AUTH$CURRENT_USER") {
                accept(ContentType.Application.Json)
            }
        }
    }

    override suspend fun clearCachedTokens() {
        client.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
