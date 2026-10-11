package com.ua.pavliyk.pager.data.posts

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.common.map
import com.ua.pavliyk.pager.data.posts.model.requests.NewPost
import com.ua.pavliyk.pager.data.posts.model.responses.Post
import com.ua.pavliyk.pager.data.posts.model.responses.Posts
import com.ua.pavliyk.pager.data.posts.service.PostApiService
import com.ua.pavliyk.pager.domain.posts.PostRepository

internal class AppPostRepository(
    private val postApiService: PostApiService
) : PostRepository {

    override suspend fun getAllPosts(): Result<Posts> {
        return postApiService.getAllPosts()
    }

    override suspend fun addPost(post: NewPost): Result<String> {
        return postApiService.addPost(post).map {
            it.toString()
        }
    }

    override suspend fun updatePost(post: Post): Result<String> {
        return postApiService.updatePost(post).map {
            it.toString()
        }
    }

    override suspend fun deletePost(postId: Int): Result<String> {
        return postApiService.deletePost(postId).map {
            it.toString()
        }
    }
}
