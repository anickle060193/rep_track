package com.adamnickle.reptrack.injection.module

import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.ui.post.PostListViewModel
import com.adamnickle.reptrack.ui.post.PostViewModel
import com.adamnickle.reptrack.ui.workouts.WorkoutsListViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey( WorkoutsListViewModel::class )
    abstract fun bindWorkoutsListViewModel( workoutsListViewModel: WorkoutsListViewModel ): ViewModel
}