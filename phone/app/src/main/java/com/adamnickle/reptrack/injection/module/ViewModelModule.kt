package com.adamnickle.reptrack.injection.module

import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.ui.exercise.ExerciseSetsListViewModel
import com.adamnickle.reptrack.ui.exerciseSet.ExerciseSetViewModel
import com.adamnickle.reptrack.ui.workout.ExercisesListViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey( ExercisesListViewModel::class )
    abstract fun bindExercisesListViewModel( exercisesListViewModel: ExercisesListViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( ExerciseSetsListViewModel::class )
    abstract fun bindExerciseSetsListViewModel( exerciseSetsListViewModel: ExerciseSetsListViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( ExerciseSetViewModel::class )
    abstract fun bindExerciseSetViewModel( exerciseSetViewModel: ExerciseSetViewModel ): ViewModel
}