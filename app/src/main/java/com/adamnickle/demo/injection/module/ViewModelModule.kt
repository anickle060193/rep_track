package com.adamnickle.demo.injection.module

import android.arch.lifecycle.ViewModel
import com.adamnickle.demo.ui.post.PostListViewModel
import com.adamnickle.demo.ui.post.PostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule
{
    @Binds
    @IntoMap
    @ViewModelKey( PostViewModel::class )
    abstract fun bindPostViewModel( postViewModel: PostViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( PostListViewModel::class )
    abstract fun bindPostListViewModel( postListViewModel: PostListViewModel ): ViewModel
}