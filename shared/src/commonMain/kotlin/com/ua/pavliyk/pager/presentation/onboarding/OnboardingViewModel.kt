package com.ua.pavliyk.pager.presentation.onboarding

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.domain.onboarding.CompleteOnboardingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@Stable
class OnboardingViewModel internal constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("OnboardingViewModel")

    private val _state = MutableStateFlow(OnboardingState())
    internal val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>(capacity = Channel.BUFFERED)
    internal val events: Flow<OnboardingEvent> = _events.receiveAsFlow()

    internal fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnPageChanged -> onPageChanged(action.page)
            OnboardingAction.OnSkip -> {
                logger.i { "Onboarding skipped on page ${_state.value.currentPage}" }
                completeOnboarding()
            }
            OnboardingAction.OnFinish -> completeOnboarding()
        }
    }

    private fun onPageChanged(page: Int) {
        logger.i { "Current page: $page" }
        _state.update { it.copy(currentPage = page) }
    }

    private fun completeOnboarding() {
        logger.i { "Onboarding completed" }
        completeOnboardingUseCase()
        _events.trySend(OnboardingEvent.NavigateToAuth)
    }
}
