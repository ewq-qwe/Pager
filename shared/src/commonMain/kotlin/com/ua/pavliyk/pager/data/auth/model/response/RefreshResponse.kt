package com.ua.pavliyk.pager.data.auth.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
