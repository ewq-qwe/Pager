package com.ua.pavliyk.pager.presentation.onboarding

internal sealed class OnboardingAction {
    data class OnPageChanged(val page: Int) : OnboardingAction()
    data object OnSkip : OnboardingAction()
    data object OnFinish : OnboardingAction()
}
