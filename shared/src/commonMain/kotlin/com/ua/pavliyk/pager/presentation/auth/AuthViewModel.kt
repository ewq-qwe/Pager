package com.ua.pavliyk.pager.presentation.auth

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.domain.auth.login.LoginUseCase
import com.ua.pavliyk.pager.domain.auth.validation.ValidatePasswordUseCase
import com.ua.pavliyk.pager.domain.auth.validation.ValidateUsernameUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
class AuthViewModel internal constructor(
    private val validateUsernameUseCase: ValidateUsernameUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("AuthViewModel")

    private val _state = MutableStateFlow(AuthState())
    internal val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _events = Channel<AuthEvent>(capacity = Channel.BUFFERED)
    internal val events: Flow<AuthEvent> = _events.receiveAsFlow()

    internal fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.OnUsernameChange -> _state.update {
                it.copy(
                    username = action.username,
                    usernameError = if (it.isSubmitted) validateUsernameUseCase(action.username) else null,
                )
            }
            is AuthAction.OnPasswordChange -> _state.update {
                it.copy(
                    password = action.password,
                    passwordError = if (it.isSubmitted) validatePasswordUseCase(action.password) else null,
                )
            }
            AuthAction.OnTogglePasswordVisibility -> _state.update {
                it.copy(isPasswordVisible = !it.isPasswordVisible)
            }
            AuthAction.OnSignIn -> signIn()
        }
    }

    private fun signIn() {
        val current = _state.value
        if (current.isLoading) return

        val usernameError = validateUsernameUseCase(current.username)
        val passwordError = validatePasswordUseCase(current.password)

        _state.update {
            it.copy(
                usernameError = usernameError,
                passwordError = passwordError,
                isSubmitted = true,
            )
        }

        if (usernameError != null || passwordError != null) {
            logger.d { "Validation failed: username = $usernameError, password = $passwordError" }
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = loginUseCase(current.username, current.password)) {
                is Result.Success -> {
                    logger.i { "Signed in as ${current.username.trim()}" }
                    _events.trySend(AuthEvent.NavigateToHome)
                }
                is Result.Failure -> {
                    logger.e { "Sign in failed: ${result.errorMessage}" }
                    _events.trySend(AuthEvent.ShowError(result.toAuthError()))
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}
