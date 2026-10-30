package com.ua.pavliyk.pager.presentation.onboarding

internal sealed class OnboardingEvent {
    data object NavigateToAuth : OnboardingEvent()
}
