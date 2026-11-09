package com.ua.pavliyk.pager.presentation.home

internal sealed class HomeEvent {
    data object NavigateToPosts : HomeEvent()
    data object NavigateToAuth : HomeEvent()
}
