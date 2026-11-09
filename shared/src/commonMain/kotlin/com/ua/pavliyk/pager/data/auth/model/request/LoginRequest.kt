package com.ua.pavliyk.pager.data.auth.model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int = TOKEN_LIFETIME_MINUTES,
)

internal const val TOKEN_LIFETIME_MINUTES = 30
