package com.ua.pavliyk.pager.data.auth

import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.data.auth.model.response.UserResponse
import com.ua.pavliyk.pager.data.auth.service.AuthApiService
import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.common.map
import com.ua.pavliyk.pager.data.common.onFailure
import com.ua.pavliyk.pager.data.security.SecureStorage
import com.ua.pavliyk.pager.domain.auth.AuthRepository

internal class AppAuthRepository(
    private val secureStorage: SecureStorage,
    private val authApiService: AuthApiService,
) : AuthRepository {

    private val logger = Logger.withTag("AuthRepository")

    override suspend fun login(username: String, password: String): Result<Unit> {
        val result = authApiService.login(username, password)
        if (result is Result.Success) {
            saveTokens(result.data.accessToken, result.data.refreshToken)
            authApiService.clearCachedTokens()
            logger.i { "Logged in as ${result.data.username}, tokens saved" }
        }
        return result.map { }
    }

    override suspend fun refreshTokens(): Result<Unit> {
        val refreshToken = secureStorage.getRefreshToken()
        if (refreshToken.isEmpty()) {
            return Result.Failure("No refresh token", 401)
        }
        val result = authApiService.refresh(refreshToken)
        if (result is Result.Success) {
            saveTokens(result.data.accessToken, result.data.refreshToken)
            logger.i { "Tokens refreshed" }
        }
        return result.map { }.onFailure {
            logger.w { "Token refresh failed: $it" }
            secureStorage.clearTokens()
        }
    }

    override suspend fun getCurrentUser(): Result<UserResponse> = authApiService.getCurrentUser()

    override suspend fun isAuthorized(): Boolean = secureStorage.getAccessToken().isNotEmpty()

    override suspend fun logout() {
        secureStorage.clearTokens()
        authApiService.clearCachedTokens()
        logger.i { "Logged out, tokens removed" }
    }

    private suspend fun saveTokens(accessToken: String, refreshToken: String) {
        secureStorage.setAccessToken(accessToken)
        secureStorage.setRefreshToken(refreshToken)
    }
}
