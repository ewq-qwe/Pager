package com.ua.pavliyk.pager.domain.auth.user

import com.ua.pavliyk.pager.data.auth.model.response.UserResponse
import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.domain.auth.AuthRepository

internal class GetCurrentUserUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<UserResponse> = authRepository.getCurrentUser()
}
