package com.ua.pavliyk.pager.domain.onboarding

interface OnboardingRepository {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(isCompleted: Boolean)
}
