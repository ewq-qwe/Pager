package com.ua.pavliyk.pager.presentation.auth

internal sealed class AuthEvent {
    data object NavigateToPosts : AuthEvent()
}
