package com.adamnickle.demo.injection.module

import android.arch.persistence.room.Room
import com.adamnickle.demo.DemoApplication
import com.adamnickle.demo.model.database.AppDatabase
import com.adamnickle.demo.model.post.PostDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule
{
    @Provides
    @Singleton
    fun provideDatabase( app: DemoApplication ): AppDatabase = Room
                .databaseBuilder( app, AppDatabase::class.java, "posts" )
                .build()

    @Provides
    @Singleton
    fun providePostsDao( database: AppDatabase ): PostDao = database.postDao()
}