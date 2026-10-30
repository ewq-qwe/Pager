package com.ua.pavliyk.pager.domain.onboarding

class CheckOnboardingCompletedUseCase(
    private val onboardingRepository: OnboardingRepository,
) {
    operator fun invoke(): Boolean = onboardingRepository.isOnboardingCompleted()
}
