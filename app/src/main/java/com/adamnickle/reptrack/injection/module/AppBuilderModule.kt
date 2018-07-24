package com.adamnickle.reptrack.injection.module

import com.adamnickle.reptrack.MainActivity
import com.adamnickle.reptrack.ui.workouts.WorkoutsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress( "unused" )
@Module
abstract class AppBuilderModule
{
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeWorkoutsListFragment(): WorkoutsListFragment
}