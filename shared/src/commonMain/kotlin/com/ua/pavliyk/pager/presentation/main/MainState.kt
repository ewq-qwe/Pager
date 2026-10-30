package com.ua.pavliyk.pager.presentation.main

import androidx.compose.runtime.Immutable
import com.ua.pavliyk.pager.presentation.navigation.Route

@Immutable
internal data class MainState(
    val startRoute: Route? = null,
)
