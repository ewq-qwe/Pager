package com.ua.pavliyk.pager.presentation.home

import androidx.compose.runtime.Immutable

@Immutable
internal data class HomeState(
    val isLoading: Boolean = false,
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val isError: Boolean = false,
)
