package com.ua.pavliyk.pager.presentation.onboarding

import pager.shared.generated.resources.Res
import pager.shared.generated.resources.badge_dummyjson
import pager.shared.generated.resources.ic_arrow_forward
import pager.shared.generated.resources.ic_lightbulb
import pager.shared.generated.resources.onboarding_hint
import pager.shared.generated.resources.onboarding_page_counter
import pager.shared.generated.resources.ic_onboarding_create
import pager.shared.generated.resources.ic_onboarding_done
import pager.shared.generated.resources.ic_onboarding_edit
import pager.shared.generated.resources.ic_onboarding_posts
import pager.shared.generated.resources.onboarding_back
import pager.shared.generated.resources.onboarding_description_1
import pager.shared.generated.resources.onboarding_description_2
import pager.shared.generated.resources.onboarding_description_3
import pager.shared.generated.resources.onboarding_description_4
import pager.shared.generated.resources.onboarding_next
import pager.shared.generated.resources.onboarding_skip
import pager.shared.generated.resources.onboarding_start
import pager.shared.generated.resources.onboarding_title_1
import pager.shared.generated.resources.onboarding_title_2
import pager.shared.generated.resources.onboarding_title_3
import pager.shared.generated.resources.onboarding_title_4
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import co.touchlab.kermit.Logger
import com.ua.pavliyk.pager.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

@Immutable
private data class OnboardingPage(
    val title: StringResource,
    val description: StringResource,
    val image: DrawableResource,
    val endpoint: String,
)

private val onboardingPages = listOf(
    OnboardingPage(Res.string.onboarding_title_1, Res.string.onboarding_description_1, Res.drawable.ic_onboarding_posts, "GET /posts"),
    OnboardingPage(Res.string.onboarding_title_2, Res.string.onboarding_description_2, Res.drawable.ic_onboarding_create, "POST /posts/add"),
    OnboardingPage(Res.string.onboarding_title_3, Res.string.onboarding_description_3, Res.drawable.ic_onboarding_edit, "PUT · DELETE /posts/{id}"),
    OnboardingPage(Res.string.onboarding_title_4, Res.string.onboarding_description_4, Res.drawable.ic_onboarding_done, "settings: onboarding = true"),
)

private val logger = Logger.withTag("OnboardingScreen")

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            logger.i { "Current page: $page" }
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(vertical = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Pill(
                text = stringResource(Res.string.badge_dummyjson),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            TextButton(
                onClick = {
                    logger.i { "Onboarding skipped on page ${pagerState.currentPage}" }
                    onFinish()
                },
                enabled = !isLastPage,
            ) {
                Text(stringResource(Res.string.onboarding_skip))
            }
        }

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.weight(1F),
        ) { page ->
            OnboardingPageItem(
                page = onboardingPages[page],
                pageNumber = page + 1,
                pageCount = pagerState.pageCount,
                modifier = Modifier.pageTransition(pagerState, page),
            )
        }

        PagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(vertical = 16.dp),
        )

        OnboardingHint(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            OutlinedButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = pagerState.currentPage > 0,
                modifier = Modifier.weight(1F),
            ) {
                Text(stringResource(Res.string.onboarding_back))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.weight(1F),
            ) {
                Text(
                    stringResource(
                        if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_forward),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageItem(
    page: OnboardingPage,
    pageNumber: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Pill(
                text = page.endpoint,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(page.image),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(72.dp),
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(page.title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(page.description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Pill(
                text = stringResource(Res.string.onboarding_page_counter, pageNumber, pageCount),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun OnboardingHint(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_lightbulb),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(Res.string.onboarding_hint),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
internal fun Pill(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val isSelected = pagerState.currentPage == iteration
            val color by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            )
            val width by animateDpAsState(if (isSelected) 24.dp else 10.dp)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .height(10.dp)
                    .width(width)
            )
        }
    }
}

private fun Modifier.pageTransition(pagerState: PagerState, page: Int): Modifier =
    graphicsLayer {
        val pageOffset = (
            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        ).absoluteValue
        val fraction = 1F - pageOffset.coerceIn(0F, 1F)

        alpha = lerp(start = 0.3F, stop = 1F, fraction = fraction)
        val scale = lerp(start = 0.8F, stop = 1F, fraction = fraction)
        scaleX = scale
        scaleY = scale
    }

@Preview
@Composable
private fun OnboardingScreenPreview() {
    AppTheme {
        OnboardingScreen(onFinish = {})
    }
}
