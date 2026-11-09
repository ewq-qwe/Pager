package com.ua.pavliyk.pager.presentation.auth

import androidx.compose.runtime.Immutable
import com.ua.pavliyk.pager.domain.auth.validation.PasswordError
import com.ua.pavliyk.pager.domain.auth.validation.UsernameError

@Immutable
internal data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false,
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
)
