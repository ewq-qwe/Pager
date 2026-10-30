package com.ua.pavliyk.pager.data.onboarding

import com.ua.pavliyk.pager.data.settings.AppSettings
import com.ua.pavliyk.pager.domain.onboarding.OnboardingRepository

class AppOnboardingRepository(
    private val appSettings: AppSettings,
) : OnboardingRepository {

    override fun isOnboardingCompleted(): Boolean = appSettings.isOnboardingCompleted

    override fun setOnboardingCompleted(isCompleted: Boolean) {
        appSettings.isOnboardingCompleted = isCompleted
    }
}
