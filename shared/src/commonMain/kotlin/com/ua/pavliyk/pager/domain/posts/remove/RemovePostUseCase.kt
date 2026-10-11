package com.ua.pavliyk.pager.domain.posts.remove

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.domain.posts.PostRepository

internal class RemovePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: Int): Result<String> {
        return postRepository.deletePost(postId)
    }
}
