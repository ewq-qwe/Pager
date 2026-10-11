package com.ua.pavliyk.pager.presentation.onboarding

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.data.settings.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Stable
class OnboardingViewModel(
    private val appSettings: AppSettings,
) : ViewModel() {

    private val logger = Logger.withTag("OnboardingViewModel")

    private val _isOnboardingCompleted = MutableStateFlow(appSettings.isOnboardingCompleted)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    fun completeOnboarding() {
        logger.i { "Onboarding completed" }
        appSettings.isOnboardingCompleted = true
        _isOnboardingCompleted.value = true
    }

    fun showOnboardingAgain() {
        logger.i { "Onboarding requested again" }
        appSettings.isOnboardingCompleted = false
        _isOnboardingCompleted.value = false
    }
}
