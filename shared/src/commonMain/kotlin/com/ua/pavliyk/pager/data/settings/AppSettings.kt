package com.ua.pavliyk.pager.data.settings

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class AppSettings(
    private val settings: Settings,
) {

    private val logger = Logger.withTag("AppSettings")

    var isOnboardingCompleted: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) {
            logger.i { "isOnboardingCompleted = $value" }
            settings[KEY_ONBOARDING_COMPLETED] = value
        }

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}
