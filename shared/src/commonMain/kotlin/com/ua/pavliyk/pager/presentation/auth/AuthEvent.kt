package com.ua.pavliyk.pager.presentation.auth

internal sealed class AuthEvent {
    data object NavigateToHome : AuthEvent()
    data class ShowError(val error: AuthError) : AuthEvent()
}
