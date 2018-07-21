package com.adamnickle.demo.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.adamnickle.demo.model.Post
import com.adamnickle.demo.model.PostDao

@Database( entities = [
    Post::class
], version = 1, exportSchema = false )
abstract class AppDatabase: RoomDatabase()
{
    abstract fun postDao(): PostDao
}