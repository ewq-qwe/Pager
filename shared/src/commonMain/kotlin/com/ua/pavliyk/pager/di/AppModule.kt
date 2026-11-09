package com.ua.pavliyk.pager.di

import co.touchlab.kermit.Logger as KermitLogger
import com.russhwolf.settings.Settings
import com.ua.pavliyk.pager.data.auth.AppAuthRepository
import com.ua.pavliyk.pager.data.auth.service.AppAuthApiService
import com.ua.pavliyk.pager.data.auth.service.AuthApiService
import com.ua.pavliyk.pager.data.auth.service.LOGIN
import com.ua.pavliyk.pager.data.auth.service.REFRESH
import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.onboarding.AppOnboardingRepository
import com.ua.pavliyk.pager.data.security.AppSecureStorage
import com.ua.pavliyk.pager.data.security.SecureStorage
import com.ua.pavliyk.pager.data.settings.AppSettings
import com.ua.pavliyk.pager.domain.auth.AuthRepository
import com.ua.pavliyk.pager.domain.auth.login.LoginUseCase
import com.ua.pavliyk.pager.domain.auth.logout.LogoutUseCase
import com.ua.pavliyk.pager.domain.auth.session.IsUserAuthorizedUseCase
import com.ua.pavliyk.pager.domain.auth.user.GetCurrentUserUseCase
import com.ua.pavliyk.pager.domain.auth.validation.ValidatePasswordUseCase
import com.ua.pavliyk.pager.domain.auth.validation.ValidateUsernameUseCase
import com.ua.pavliyk.pager.domain.onboarding.CheckOnboardingCompletedUseCase
import com.ua.pavliyk.pager.domain.onboarding.CompleteOnboardingUseCase
import com.ua.pavliyk.pager.domain.onboarding.OnboardingRepository
import com.ua.pavliyk.pager.domain.onboarding.ResetOnboardingUseCase
import com.ua.pavliyk.pager.presentation.auth.AuthViewModel
import com.ua.pavliyk.pager.presentation.home.HomeViewModel
import com.ua.pavliyk.pager.presentation.main.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
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

val secureModule = module {
    includes(platformModule)
    singleOf(::AppSecureStorage) { bind<SecureStorage>() }
}

val networkModule = module {
    includes(secureModule)
    single {
        val secureStorage: SecureStorage = get()
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
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
            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = secureStorage.getAccessToken()
                        if (accessToken.isEmpty()) {
                            null
                        } else {
                            BearerTokens(accessToken, secureStorage.getRefreshToken())
                        }
                    }
                    refreshTokens {
                        val authRepository: AuthRepository = get()
                        when (authRepository.refreshTokens()) {
                            is Result.Success -> BearerTokens(
                                secureStorage.getAccessToken(),
                                secureStorage.getRefreshToken(),
                            )
                            is Result.Failure -> null
                        }
                    }
                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        !path.endsWith(LOGIN) && !path.endsWith(REFRESH)
                    }
                }
            }
        }
    }

    singleOf(::AppAuthApiService) { bind<AuthApiService>() }
    singleOf(::AppAuthRepository) { bind<AuthRepository>() }
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
    factoryOf(::LoginUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::IsUserAuthorizedUseCase)
    factoryOf(::GetCurrentUserUseCase)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}

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
