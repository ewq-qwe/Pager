package com.ua.pavliyk.pager.domain.posts.edit

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.posts.model.responses.Post
import com.ua.pavliyk.pager.domain.posts.PostRepository

internal class EditPostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(post: Post): Result<String> {
        return postRepository.updatePost(post)
    }
}
