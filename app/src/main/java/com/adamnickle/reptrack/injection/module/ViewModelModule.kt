package com.adamnickle.reptrack.injection.module

import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.ui.workouts.WorkoutsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress( "unused" )
@Module
abstract class ViewModelModule
{
    @Binds
    @IntoMap
    @ViewModelKey( WorkoutsListViewModel::class )
    abstract fun bindWorkoutsListViewModel( workoutsListViewModel: WorkoutsListViewModel ): ViewModel
}