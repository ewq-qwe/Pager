package com.ua.pavliyk.pager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ua.pavliyk.pager.presentation.PostsScreen
import com.ua.pavliyk.pager.presentation.auth.AuthRoute
import com.ua.pavliyk.pager.presentation.main.MainAction
import com.ua.pavliyk.pager.presentation.main.MainEvent
import com.ua.pavliyk.pager.presentation.main.MainViewModel
import com.ua.pavliyk.pager.presentation.onboarding.OnboardingRoute
import com.ua.pavliyk.pager.presentation.ui.ObserveEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    viewModel: MainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // The start screen depends on whether the onboarding has already been completed.
    val startRoute = state.startRoute ?: return

    val backStack = rememberNavBackStack(navigationConfig, startRoute)

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            MainEvent.NavigateToOnboarding -> backStack.replaceAllWith(Route.Onboarding)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // Every screen gets its own ViewModel, which is cleared when the screen leaves the back stack.
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Route.Onboarding> {
                OnboardingRoute(
                    onNavigateToAuth = { backStack.replaceAllWith(Route.Auth) },
                )
            }
            entry<Route.Auth> {
                AuthRoute(
                    onNavigateToPosts = { backStack.replaceAllWith(Route.Posts) },
                )
            }
            entry<Route.Posts> {
                PostsScreen(
                    onShowOnboarding = { viewModel.onAction(MainAction.OnShowOnboardingAgain) },
                )
            }
        },
    )
}

// Onboarding and auth must not be reachable with the "back" button, so the new screen replaces the whole stack.
private fun NavBackStack<NavKey>.replaceAllWith(route: Route) {
    add(route)
    repeat(size - 1) { removeAt(0) }
}
