package com.ua.pavliyk.pager.presentation.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.domain.auth.logout.LogoutUseCase
import com.ua.pavliyk.pager.domain.auth.user.GetCurrentUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
class HomeViewModel internal constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val logger = Logger.withTag("HomeViewModel")

    private val _state = MutableStateFlow(HomeState())
    internal val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(capacity = Channel.BUFFERED)
    internal val events: Flow<HomeEvent> = _events.receiveAsFlow()

    init {
        loadCurrentUser()
    }

    internal fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRetry -> loadCurrentUser()
            HomeAction.OnOpenPosts -> _events.trySend(HomeEvent.NavigateToPosts)
            HomeAction.OnLogout -> logout()
        }
    }

    private fun loadCurrentUser() {
        _state.update { it.copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        fullName = "${result.data.firstName} ${result.data.lastName}",
                        username = result.data.username,
                        email = result.data.email,
                    )
                }
                is Result.Failure -> {
                    logger.e { "Loading current user failed: ${result.errorMessage}" }
                    if (result.code == 401) {
                        logout()
                    } else {
                        _state.update { it.copy(isLoading = false, isError = true) }
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _events.trySend(HomeEvent.NavigateToAuth)
        }
    }
}
