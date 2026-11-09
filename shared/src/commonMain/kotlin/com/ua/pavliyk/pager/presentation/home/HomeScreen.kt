package com.ua.pavliyk.pager.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ua.pavliyk.pager.presentation.onboarding.Pill
import com.ua.pavliyk.pager.presentation.ui.ObserveEvents
import com.ua.pavliyk.pager.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pager.shared.generated.resources.Res
import pager.shared.generated.resources.badge_dummyjson
import pager.shared.generated.resources.home_error
import pager.shared.generated.resources.home_greeting
import pager.shared.generated.resources.home_logout
import pager.shared.generated.resources.home_open_posts
import pager.shared.generated.resources.home_retry
import pager.shared.generated.resources.home_title
import pager.shared.generated.resources.ic_person

@Composable
fun HomeRoute(
    onNavigateToPosts: () -> Unit,
    onNavigateToAuth: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            HomeEvent.NavigateToPosts -> onNavigateToPosts()
            HomeEvent.NavigateToAuth -> onNavigateToAuth()
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Pill(
            text = stringResource(Res.string.badge_dummyjson),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Start),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                    state.isError -> {
                        Text(
                            text = stringResource(Res.string.home_error),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                        TextButton(onClick = { onAction(HomeAction.OnRetry) }) {
                            Text(stringResource(Res.string.home_retry))
                        }
                    }
                    else -> UserInfo(state)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onAction(HomeAction.OnOpenPosts) },
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(stringResource(Res.string.home_open_posts), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onAction(HomeAction.OnLogout) },
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(stringResource(Res.string.home_logout))
        }
    }
}

@Composable
private fun UserInfo(state: HomeState) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_person),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(40.dp),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(Res.string.home_greeting, state.fullName),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
    )
    Text(
        text = "@${state.username}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
    )
    Text(
        text = state.email,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            state = HomeState(fullName = "Emily Johnson", username = "emilys", email = "emily.johnson@x.dummyjson.com"),
            onAction = {},
        )
    }
}
