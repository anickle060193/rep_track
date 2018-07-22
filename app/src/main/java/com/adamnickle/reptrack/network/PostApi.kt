package com.adamnickle.reptrack.network

import com.adamnickle.reptrack.model.post.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface PostApi
{
    @GET( "/posts" )
    fun getPosts(): Observable<List<Post>>
}