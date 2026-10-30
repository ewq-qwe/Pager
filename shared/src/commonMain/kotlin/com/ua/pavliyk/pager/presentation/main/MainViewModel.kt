package com.ua.pavliyk.pager.presentation.main

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.domain.onboarding.CheckOnboardingCompletedUseCase
import com.ua.pavliyk.pager.domain.onboarding.ResetOnboardingUseCase
import com.ua.pavliyk.pager.presentation.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@Stable
class MainViewModel internal constructor(
    private val checkOnboardingCompletedUseCase: CheckOnboardingCompletedUseCase,
    private val resetOnboardingUseCase: ResetOnboardingUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("MainViewModel")

    private val _state = MutableStateFlow(MainState())
    internal val state: StateFlow<MainState> = _state.asStateFlow()

    private val _events = Channel<MainEvent>(capacity = Channel.BUFFERED)
    internal val events: Flow<MainEvent> = _events.receiveAsFlow()

    init {
        resolveStartRoute()
    }

    internal fun onAction(action: MainAction) {
        when (action) {
            MainAction.OnShowOnboardingAgain -> showOnboardingAgain()
        }
    }

    private fun resolveStartRoute() {
        val isOnboardingCompleted = checkOnboardingCompletedUseCase()
        val startRoute = if (isOnboardingCompleted) Route.Auth else Route.Onboarding
        logger.i { "Onboarding completed: $isOnboardingCompleted, start route: $startRoute" }
        _state.update { it.copy(startRoute = startRoute) }
    }

    private fun showOnboardingAgain() {
        logger.i { "Onboarding requested again" }
        resetOnboardingUseCase()
        _events.trySend(MainEvent.NavigateToOnboarding)
    }
}
