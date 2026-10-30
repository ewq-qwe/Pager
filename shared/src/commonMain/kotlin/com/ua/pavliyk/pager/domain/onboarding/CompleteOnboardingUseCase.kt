package com.ua.pavliyk.pager.domain.onboarding

class CompleteOnboardingUseCase(
    private val onboardingRepository: OnboardingRepository,
) {
    operator fun invoke() {
        onboardingRepository.setOnboardingCompleted(true)
    }
}
