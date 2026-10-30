package com.ua.pavliyk.pager.domain.onboarding

class ResetOnboardingUseCase(
    private val onboardingRepository: OnboardingRepository,
) {
    operator fun invoke() {
        onboardingRepository.setOnboardingCompleted(false)
    }
}
