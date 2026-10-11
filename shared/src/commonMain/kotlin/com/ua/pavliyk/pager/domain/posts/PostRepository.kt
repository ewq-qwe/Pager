package com.ua.pavliyk.pager.domain.posts

import com.ua.pavliyk.pager.data.common.Result
import com.ua.pavliyk.pager.data.posts.model.requests.NewPost
import com.ua.pavliyk.pager.data.posts.model.responses.Post
import com.ua.pavliyk.pager.data.posts.model.responses.Posts

internal interface PostRepository {
    suspend fun getAllPosts(): Result<Posts>
    suspend fun addPost(post: NewPost): Result<String>
    suspend fun updatePost(post: Post): Result<String>
    suspend fun deletePost(postId: Int): Result<String>
}
