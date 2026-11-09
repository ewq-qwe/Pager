package com.ua.pavliyk.pager.data.auth.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String = "",
    val image: String = "",
    val accessToken: String,
    val refreshToken: String,
)
