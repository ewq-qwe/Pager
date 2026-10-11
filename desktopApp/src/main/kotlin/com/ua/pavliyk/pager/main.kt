package com.ua.pavliyk.pager

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ua.pavliyk.pager.di.initKoin
import com.ua.pavliyk.pager.presentation.App
import com.ua.pavliyk.pager.ui.theme.AppTheme


fun main() = application {
        initKoin { printLogger() }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Posts Pager",
        ) {
            App()
        }
}