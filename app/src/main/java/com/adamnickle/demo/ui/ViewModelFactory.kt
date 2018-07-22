package com.adamnickle.demo.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.adamnickle.demo.ui.post.PostListViewModel

class ViewModelFactory: ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create( modelClass: Class<T> ): T
    {
        if( modelClass.isAssignableFrom( PostListViewModel::class.java ) )
        {
            @Suppress( "UNCHECKED_CAST" )
            return PostListViewModel() as T
        }

        throw IllegalArgumentException( "Unknown ViewModel class $modelClass" )
    }
}