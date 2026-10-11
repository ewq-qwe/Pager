package com.ua.pavliyk.pager.data.posts.model.requests


import kotlinx.serialization.Serializable
import com.ua.pavliyk.pager.data.posts.model.responses.Reactions

@Serializable
internal data class NewPost(
    val body: String = "",
    val reactions: Reactions,
    val tags: List<String> = emptyList(),
    val title: String = "",
    val userId: Int,
    val views: Int = 0
)