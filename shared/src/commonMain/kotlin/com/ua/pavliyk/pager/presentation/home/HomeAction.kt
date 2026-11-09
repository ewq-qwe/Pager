package com.ua.pavliyk.pager.presentation.home

internal sealed class HomeAction {
    data object OnRetry : HomeAction()
    data object OnOpenPosts : HomeAction()
    data object OnLogout : HomeAction()
}
