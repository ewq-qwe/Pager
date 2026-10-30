package com.ua.pavliyk.pager.domain.auth.validation

enum class UsernameError {
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    INVALID_CHARACTERS,
}

class ValidateUsernameUseCase {

    operator fun invoke(username: String): UsernameError? {
        val value = username.trim()
        return when {
            value.isEmpty() -> UsernameError.EMPTY
            value.length < MIN_LENGTH -> UsernameError.TOO_SHORT
            value.length > MAX_LENGTH -> UsernameError.TOO_LONG
            !value.matches(ALLOWED_CHARACTERS) -> UsernameError.INVALID_CHARACTERS
            else -> null
        }
    }

    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 30
        private val ALLOWED_CHARACTERS = Regex("^[A-Za-z0-9._]+$")
    }
}
