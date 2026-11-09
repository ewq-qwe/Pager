package com.ua.pavliyk.pager.domain.auth.login

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.domain.auth.AuthRepository

internal class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> =
        authRepository.login(username.trim(), password)
}
