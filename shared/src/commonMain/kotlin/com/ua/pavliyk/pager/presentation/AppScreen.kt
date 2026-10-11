package com.ua.pavliyk.pager.presentation

import pager.shared.generated.resources.Res
import pager.shared.generated.resources.action_delete
import pager.shared.generated.resources.action_get
import pager.shared.generated.resources.action_post
import pager.shared.generated.resources.action_put
import pager.shared.generated.resources.action_show_onboarding
import pager.shared.generated.resources.app_name
import pager.shared.generated.resources.ic_onboarding_posts
import pager.shared.generated.resources.ic_thumb_down
import pager.shared.generated.resources.ic_thumb_up
import pager.shared.generated.resources.posts_empty
import pager.shared.generated.resources.posts_section_title
import pager.shared.generated.resources.posts_subtitle
import pager.shared.generated.resources.response_title
import pager.shared.generated.resources.rest_runner_title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ua.pavliyk.pager.presentation.onboarding.OnboardingScreen
import com.ua.pavliyk.pager.presentation.onboarding.OnboardingViewModel
import com.ua.pavliyk.pager.presentation.onboarding.Pill
import com.ua.pavliyk.pager.data.posts.model.responses.Post
import com.ua.pavliyk.pager.presentation.ui.ObserveEvents
import com.ua.pavliyk.pager.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    onboardingViewModel: OnboardingViewModel = koinViewModel(),
) {
    AppTheme {
        val isOnboardingCompleted by onboardingViewModel.isOnboardingCompleted.collectAsStateWithLifecycle()

        if (isOnboardingCompleted) {
            PostsScreen(
                onShowOnboarding = onboardingViewModel::showOnboardingAgain
            )
        } else {
            OnboardingScreen(
                onFinish = onboardingViewModel::completeOnboarding
            )
        }
    }
}

@Composable
private fun PostsScreen(
    onShowOnboarding: () -> Unit,
    viewModel: AppViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            is AppEvent.ShowDeleteErrorSnackbar -> {
                showSnackbar(coroutineScope, snackbarHostState, event.errorMessage)
            }
            is AppEvent.ShowGetErrorSnackbar -> {
                showSnackbar(coroutineScope, snackbarHostState, event.errorMessage)
            }
            is AppEvent.ShowPostErrorSnackbar -> {
                showSnackbar(coroutineScope, snackbarHostState, event.errorMessage)
            }
            is AppEvent.ShowPutErrorSnackbar -> {
                showSnackbar(coroutineScope, snackbarHostState, event.errorMessage)
            }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppContent(
            state = state,
            onGet = {
                viewModel.onAction(AppAction.OnFetchPosts)
            },
            onPost = {
                viewModel.onAction(AppAction.OnCreatePost)
            },
            onPut = {
                viewModel.onAction(AppAction.OnUpdatePost)
            },
            onDelete = {
                viewModel.onAction(AppAction.OnDeletePost)
            },
            onShowOnboarding = onShowOnboarding
        )
    }
}

private fun showSnackbar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: String
) {
    scope.launch {
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
    }
}

@Composable
private fun AppContent(
    state: AppState,
    onGet: () -> Unit,
    onPost: () -> Unit,
    onPut: () -> Unit,
    onDelete: () -> Unit,
    onShowOnboarding: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { PostsHeader() }

        item {
            RestRunnerCard(
                isProgressVisible = state.isProgressVisible,
                onGet = onGet,
                onPost = onPost,
                onPut = onPut,
                onDelete = onDelete,
                onShowOnboarding = onShowOnboarding,
            )
        }

        state.error?.let {
            item {
                ResponseCard(
                    text = it,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }

        state.result?.let {
            item {
                ResponseCard(
                    text = it,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.posts_section_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Pill(
                    text = state.posts.size.toString(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        if (state.posts.isEmpty() && !state.isProgressVisible) {
            item {
                Text(
                    text = stringResource(Res.string.posts_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(state.posts, key = { it.id }) { post ->
            PostCard(post)
        }
    }
}

@Composable
private fun PostsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_onboarding_posts),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(Res.string.posts_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Pill(
            text = "DUMMYJSON",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
private fun RestRunnerCard(
    isProgressVisible: Boolean,
    onGet: () -> Unit,
    onPost: () -> Unit,
    onPut: () -> Unit,
    onDelete: () -> Unit,
    onShowOnboarding: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.rest_runner_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                MethodButton(
                    label = Res.string.action_get,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = onGet,
                    modifier = Modifier.weight(1F),
                )
                Spacer(modifier = Modifier.width(8.dp))
                MethodButton(
                    label = Res.string.action_post,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onPost,
                    modifier = Modifier.weight(1F),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                MethodButton(
                    label = Res.string.action_put,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    onClick = onPut,
                    modifier = Modifier.weight(1F),
                )
                Spacer(modifier = Modifier.width(8.dp))
                MethodButton(
                    label = Res.string.action_delete,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    onClick = onDelete,
                    modifier = Modifier.weight(1F),
                )
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onShowOnboarding() }
            ) {
                Text(stringResource(Res.string.action_show_onboarding))
            }
            if (isProgressVisible) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun MethodButton(
    label: StringResource,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Text(stringResource(label), fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ResponseCard(
    text: String,
    containerColor: Color,
    contentColor: Color,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.response_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 8,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PostCard(post: Post) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Pill(
                    text = "GET",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "#${post.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                post.tags.forEach { tag ->
                    Pill(
                        text = "#$tag",
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15F),
                        contentColor = MaterialTheme.colorScheme.tertiary,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Reaction(Res.drawable.ic_thumb_up, post.reactions.likes)
                Spacer(modifier = Modifier.width(16.dp))
                Reaction(Res.drawable.ic_thumb_down, post.reactions.dislikes)
            }
        }
    }
}

@Composable
private fun Reaction(icon: DrawableResource, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun AppContentPreview() {
    AppTheme {
        AppContent(AppState(), {}, {}, {}, {}, {})
    }
}
