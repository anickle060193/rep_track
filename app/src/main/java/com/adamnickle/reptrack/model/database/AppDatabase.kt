package com.adamnickle.reptrack.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.adamnickle.reptrack.model.post.Post
import com.adamnickle.reptrack.model.post.PostDao

@Database( entities = [
    Post::class
], version = 1, exportSchema = false )
abstract class AppDatabase: RoomDatabase()
{
    abstract fun postDao(): PostDao
}