package com.adamnickle.demo.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.adamnickle.demo.model.post.Post
import com.adamnickle.demo.model.post.PostDao

@Database( entities = [
    Post::class
], version = 1, exportSchema = false )
abstract class AppDatabase: RoomDatabase()
{
    abstract fun postDao(): PostDao
}