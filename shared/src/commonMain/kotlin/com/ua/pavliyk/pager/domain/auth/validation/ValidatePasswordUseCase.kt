package com.ua.pavliyk.pager.domain.auth.validation

enum class PasswordError {
    EMPTY,
    TOO_SHORT,
    CONTAINS_SPACES,
}

class ValidatePasswordUseCase {

    operator fun invoke(password: String): PasswordError? = when {
        password.isEmpty() -> PasswordError.EMPTY
        password.any { it.isWhitespace() } -> PasswordError.CONTAINS_SPACES
        password.length < MIN_LENGTH -> PasswordError.TOO_SHORT
        else -> null
    }

    companion object {
        const val MIN_LENGTH = 8
    }
}
