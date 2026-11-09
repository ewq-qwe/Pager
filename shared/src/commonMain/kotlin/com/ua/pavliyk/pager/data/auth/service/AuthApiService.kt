package com.ua.pavliyk.pager.data.auth.service

import com.ua.pavliyk.pager.data.auth.model.response.LoginResponse
import com.ua.pavliyk.pager.data.auth.model.response.RefreshResponse
import com.ua.pavliyk.pager.data.auth.model.response.UserResponse
import com.ua.pavliyk.pager.data.common.Result

internal const val AUTH = "auth"
internal const val LOGIN = "/login"
internal const val REFRESH = "/refresh"
internal const val CURRENT_USER = "/me"

internal interface AuthApiService {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun refresh(refreshToken: String): Result<RefreshResponse>
    suspend fun getCurrentUser(): Result<UserResponse>
    suspend fun clearCachedTokens()
}
