package com.ua.pavliyk.pager

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.ua.pavliyk.pager.di.initKoin
import com.ua.pavliyk.pager.presentation.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin { printLogger() }
    ComposeViewport {
        App()
    }
}