package com.ua.pavliyk.pager.presentation.auth

import com.ua.pavliyk.pager.data.common.Result

internal enum class AuthError {
    INVALID_CREDENTIALS,
    NETWORK,
    UNKNOWN,
}

internal fun Result.Failure.toAuthError(): AuthError = when (code) {
    null -> AuthError.NETWORK
    400, 401 -> AuthError.INVALID_CREDENTIALS
    else -> AuthError.UNKNOWN
}
