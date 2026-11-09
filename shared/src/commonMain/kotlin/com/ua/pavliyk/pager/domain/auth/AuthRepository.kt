package com.ua.pavliyk.pager.domain.auth

import com.ua.pavliyk.pager.data.auth.model.response.UserResponse
import com.ua.pavliyk.pager.data.common.Result

internal interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun refreshTokens(): Result<Unit>
    suspend fun getCurrentUser(): Result<UserResponse>
    suspend fun isAuthorized(): Boolean
    suspend fun logout()
}
