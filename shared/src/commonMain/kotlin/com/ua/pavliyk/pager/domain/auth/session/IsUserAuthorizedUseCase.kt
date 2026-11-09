package com.ua.pavliyk.pager.domain.auth.session

import com.ua.pavliyk.pager.domain.auth.AuthRepository

internal class IsUserAuthorizedUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Boolean = authRepository.isAuthorized()
}
