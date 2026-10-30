package com.ua.pavliyk.pager.presentation.auth

internal sealed class AuthAction {
    data class OnUsernameChange(val username: String) : AuthAction()
    data class OnPasswordChange(val password: String) : AuthAction()
    data object OnTogglePasswordVisibility : AuthAction()
    data object OnSignIn : AuthAction()
}
