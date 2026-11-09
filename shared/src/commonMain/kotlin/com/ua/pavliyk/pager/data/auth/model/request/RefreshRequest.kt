package com.ua.pavliyk.pager.data.auth.model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = TOKEN_LIFETIME_MINUTES,
)
