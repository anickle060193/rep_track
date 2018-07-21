package com.adamnickle.demo.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface PostDao
{
    @get:Query( "SELECT * FROM Post" )
    val all: List<Post>

    @Insert
    fun insertAll( vararg posts: Post )
}