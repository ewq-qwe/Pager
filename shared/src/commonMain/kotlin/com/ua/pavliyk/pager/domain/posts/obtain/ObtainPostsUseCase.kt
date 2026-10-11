package com.ua.pavliyk.pager.domain.posts.obtain

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.posts.model.responses.Posts
import com.ua.pavliyk.pager.domain.posts.PostRepository

internal class ObtainPostsUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(): Result<Posts> {
        return postRepository.getAllPosts()
    }
}
