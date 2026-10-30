package com.ua.pavliyk.pager.di

import co.touchlab.kermit.Logger as KermitLogger
import com.russhwolf.settings.Settings
import com.ua.pavliyk.pager.data.onboarding.AppOnboardingRepository
import com.ua.pavliyk.pager.data.settings.AppSettings
import com.ua.pavliyk.pager.domain.auth.validation.ValidatePasswordUseCase
import com.ua.pavliyk.pager.domain.auth.validation.ValidateUsernameUseCase
import com.ua.pavliyk.pager.domain.onboarding.CheckOnboardingCompletedUseCase
import com.ua.pavliyk.pager.domain.onboarding.CompleteOnboardingUseCase
import com.ua.pavliyk.pager.domain.onboarding.OnboardingRepository
import com.ua.pavliyk.pager.domain.onboarding.ResetOnboardingUseCase
import com.ua.pavliyk.pager.presentation.auth.AuthViewModel
import com.ua.pavliyk.pager.presentation.main.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.ua.pavliyk.pager.data.posts.AppPostRepository
import com.ua.pavliyk.pager.data.posts.service.AppPostApiService
import com.ua.pavliyk.pager.data.posts.service.PostApiService
import com.ua.pavliyk.pager.domain.posts.PostRepository
import com.ua.pavliyk.pager.domain.posts.create.CreatePostUseCase
import com.ua.pavliyk.pager.domain.posts.edit.EditPostUseCase
import com.ua.pavliyk.pager.domain.posts.obtain.ObtainPostsUseCase
import com.ua.pavliyk.pager.domain.posts.remove.RemovePostUseCase
import com.ua.pavliyk.pager.presentation.AppViewModel
import com.ua.pavliyk.pager.presentation.onboarding.OnboardingViewModel

val networkModule = module {
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        KermitLogger.withTag("Ktor").d { message }
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    singleOf(::AppPostApiService) { bind<PostApiService>() }
}

val settingsModule = module {
    single<Settings> { Settings() }
    singleOf(::AppSettings)
}

val onboardingModule = module {
    singleOf(::AppOnboardingRepository) { bind<OnboardingRepository>() }
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::CheckOnboardingCompletedUseCase)
    factoryOf(::ResetOnboardingUseCase)
    viewModelOf(::MainViewModel)
}

val authModule = module {
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    viewModelOf(::AuthViewModel)
}

// Declared last: top-level properties are initialized in order, so the included modules must already exist.
val appModule = module {
    includes(networkModule, settingsModule, onboardingModule, authModule)
    singleOf(::AppPostRepository) { bind<PostRepository>() }
    factoryOf(::CreatePostUseCase)
    factoryOf(::EditPostUseCase)
    factoryOf(::ObtainPostsUseCase)
    factoryOf(::RemovePostUseCase)
    viewModelOf(::AppViewModel)
    viewModelOf(::OnboardingViewModel)
}
