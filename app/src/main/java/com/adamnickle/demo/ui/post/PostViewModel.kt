package com.adamnickle.demo.ui.post

import android.arch.lifecycle.MutableLiveData
import com.adamnickle.demo.base.BaseViewModel
import com.adamnickle.demo.model.post.Post

class PostViewModel: BaseViewModel()
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