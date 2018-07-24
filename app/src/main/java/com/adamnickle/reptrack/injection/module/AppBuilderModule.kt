package com.adamnickle.reptrack.injection.module

import com.adamnickle.reptrack.MainActivity
import com.adamnickle.reptrack.ui.post.PostListActivity
import com.adamnickle.reptrack.ui.workouts.WorkoutsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributePostListActivity(): PostListActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeWorkoutsListFragment(): WorkoutsListFragment
}