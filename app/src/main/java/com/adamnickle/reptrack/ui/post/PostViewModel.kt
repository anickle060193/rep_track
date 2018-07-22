package com.adamnickle.reptrack.ui.post

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.post.Post
import javax.inject.Inject

class PostViewModel @Inject constructor(): ViewModel()
{
    private val postTitle = MutableLiveData<String>()
    private val postBody = MutableLiveData<String>()

    fun bind( post: Post)
    {
        postTitle.value = post.title
        postBody.value = post.body
    }

    fun getPostTitle(): MutableLiveData<String> = postTitle

    fun getPostBody(): MutableLiveData<String> = postBody
}