package com.ua.pavliyk.pager.domain.posts.create

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.posts.model.requests.NewPost
import com.ua.pavliyk.pager.domain.posts.PostRepository

internal class CreatePostUseCase (
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(post: NewPost): Result<String> {
        return postRepository.addPost(post)
    }
}
