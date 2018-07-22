package com.adamnickle.demo.network

import com.adamnickle.demo.model.post.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface PostApi
{
    @GET( "/posts" )
    fun getPosts(): Observable<List<Post>>
}