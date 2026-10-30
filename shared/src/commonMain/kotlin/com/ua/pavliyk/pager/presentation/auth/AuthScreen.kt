package com.ua.pavliyk.pager.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ua.pavliyk.pager.domain.auth.validation.PasswordError
import com.ua.pavliyk.pager.domain.auth.validation.UsernameError
import com.ua.pavliyk.pager.domain.auth.validation.ValidatePasswordUseCase
import com.ua.pavliyk.pager.domain.auth.validation.ValidateUsernameUseCase
import com.ua.pavliyk.pager.presentation.onboarding.Pill
import com.ua.pavliyk.pager.presentation.ui.ObserveEvents
import com.ua.pavliyk.pager.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pager.shared.generated.resources.Res
import pager.shared.generated.resources.auth_hide_password
import pager.shared.generated.resources.auth_keyboard_hint
import pager.shared.generated.resources.auth_password_label
import pager.shared.generated.resources.auth_password_placeholder
import pager.shared.generated.resources.auth_show_password
import pager.shared.generated.resources.auth_sign_in
import pager.shared.generated.resources.auth_subtitle
import pager.shared.generated.resources.auth_title
import pager.shared.generated.resources.auth_username_label
import pager.shared.generated.resources.auth_username_placeholder
import pager.shared.generated.resources.badge_dummyjson
import pager.shared.generated.resources.error_password_empty
import pager.shared.generated.resources.error_password_spaces
import pager.shared.generated.resources.error_password_too_short
import pager.shared.generated.resources.error_username_empty
import pager.shared.generated.resources.error_username_invalid
import pager.shared.generated.resources.error_username_too_long
import pager.shared.generated.resources.error_username_too_short
import pager.shared.generated.resources.ic_arrow_forward
import pager.shared.generated.resources.ic_lightbulb
import pager.shared.generated.resources.ic_lock
import pager.shared.generated.resources.ic_person
import pager.shared.generated.resources.ic_visibility
import pager.shared.generated.resources.ic_visibility_off

@Composable
fun AuthRoute(
    onNavigateToPosts: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEvents(viewModel.events) { event ->
        when (event) {
            AuthEvent.NavigateToPosts -> onNavigateToPosts()
        }
    }

    AuthScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun AuthScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val passwordFocusRequester = remember { FocusRequester() }

    // The same action for the "Sign in" button and the keyboard action of the password field.
    val signIn = {
        keyboard?.hide()
        focusManager.clearFocus()
        onAction(AuthAction.OnSignIn)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Pill(
            text = stringResource(Res.string.badge_dummyjson),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Start),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp),
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.auth_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.auth_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = state.username,
                    onValueChange = { onAction(AuthAction.OnUsernameChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.auth_username_label)) },
                    placeholder = { Text(stringResource(Res.string.auth_username_placeholder)) },
                    leadingIcon = {
                        Icon(painterResource(Res.drawable.ic_person), contentDescription = null)
                    },
                    isError = state.usernameError != null,
                    supportingText = state.usernameError?.let { { Text(it.message()) } },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Next,
                    ),
                    // Keyboard action "Next" moves the focus to the password field.
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() },
                    ),
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onAction(AuthAction.OnPasswordChange(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester),
                    label = { Text(stringResource(Res.string.auth_password_label)) },
                    placeholder = { Text(stringResource(Res.string.auth_password_placeholder)) },
                    leadingIcon = {
                        Icon(painterResource(Res.drawable.ic_lock), contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { onAction(AuthAction.OnTogglePasswordVisibility) }) {
                            Icon(
                                painter = painterResource(
                                    if (state.isPasswordVisible) Res.drawable.ic_visibility_off
                                    else Res.drawable.ic_visibility
                                ),
                                contentDescription = stringResource(
                                    if (state.isPasswordVisible) Res.string.auth_hide_password
                                    else Res.string.auth_show_password
                                ),
                            )
                        }
                    },
                    visualTransformation = if (state.isPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    isError = state.passwordError != null,
                    supportingText = state.passwordError?.let { { Text(it.message()) } },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done,
                    ),
                    // Keyboard action "Done" signs the user in, the same as the "Sign in" button.
                    keyboardActions = KeyboardActions(
                        onDone = { signIn() },
                    ),
                )

                Button(
                    onClick = signIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.auth_sign_in),
                        fontWeight = FontWeight.Bold,
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

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(),
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
                    text = stringResource(Res.string.auth_keyboard_hint),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun UsernameError.message(): String = when (this) {
    UsernameError.EMPTY -> stringResource(Res.string.error_username_empty)
    UsernameError.TOO_SHORT -> stringResource(Res.string.error_username_too_short, ValidateUsernameUseCase.MIN_LENGTH)
    UsernameError.TOO_LONG -> stringResource(Res.string.error_username_too_long, ValidateUsernameUseCase.MAX_LENGTH)
    UsernameError.INVALID_CHARACTERS -> stringResource(Res.string.error_username_invalid)
}

@Composable
private fun PasswordError.message(): String = when (this) {
    PasswordError.EMPTY -> stringResource(Res.string.error_password_empty)
    PasswordError.TOO_SHORT -> stringResource(Res.string.error_password_too_short, ValidatePasswordUseCase.MIN_LENGTH)
    PasswordError.CONTAINS_SPACES -> stringResource(Res.string.error_password_spaces)
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AppTheme {
        AuthScreen(
            state = AuthState(usernameError = UsernameError.TOO_SHORT),
            onAction = {},
        )
    }
}
