package com.adamnickle.demo.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import com.adamnickle.demo.model.database.AppDatabase
import com.adamnickle.demo.ui.post.PostListViewModel

class ViewModelFactory( private val activity: AppCompatActivity ): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create( modelClass: Class<T> ): T
    {
        if( modelClass.isAssignableFrom( PostListViewModel::class.java ) )
        {
            val db = Room.databaseBuilder( activity.applicationContext, AppDatabase::class.java, "posts" ).build()

            @Suppress( "UNCHECKED_CAST" )
            return PostListViewModel( db.postDao() ) as T
        }

        throw IllegalArgumentException( "Unknown ViewModel class $modelClass" )
    }
}