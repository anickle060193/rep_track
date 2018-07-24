package com.adamnickle.reptrack.injection.module

import com.adamnickle.reptrack.ui.post.PostListActivity
import com.adamnickle.reptrack.ui.workouts.WorkoutsListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributePostListActivity(): PostListActivity

    @ContributesAndroidInjector
    abstract fun contributeWorkoutsListActivity(): WorkoutsListActivity
}