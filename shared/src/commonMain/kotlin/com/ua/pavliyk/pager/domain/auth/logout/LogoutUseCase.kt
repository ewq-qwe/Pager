package com.ua.pavliyk.pager.domain.auth.logout

import com.ua.pavliyk.pager.domain.auth.AuthRepository

internal class LogoutUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
