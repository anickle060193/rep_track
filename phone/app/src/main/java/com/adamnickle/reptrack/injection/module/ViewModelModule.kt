package com.adamnickle.reptrack.injection.module

import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.ui.exercise.ExerciseFragmentViewModel
import com.adamnickle.reptrack.ui.exerciseSet.ExerciseSetFragmentViewModel
import com.adamnickle.reptrack.ui.workout.WorkoutFragmentViewModel
import com.adamnickle.reptrack.ui.workouts.WorkoutsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress( "unused" )
@Module
abstract class ViewModelModule
{
    @Binds
    @IntoMap
    @ViewModelKey( WorkoutsFragmentViewModel::class )
    abstract fun bindWorkoutsFragmentViewModel( workoutsFragmentViewModel: WorkoutsFragmentViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( WorkoutFragmentViewModel::class )
    abstract fun bindWorkoutFragmentViewModel( workoutFragmentViewModel: WorkoutFragmentViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( ExerciseFragmentViewModel::class )
    abstract fun bindExerciseFragmentViewModel( exerciseFragmentViewModel: ExerciseFragmentViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( ExerciseSetFragmentViewModel::class )
    abstract fun bindExerciseSetFragmentViewModel( exerciseSetFragmentViewModel: ExerciseSetFragmentViewModel ): ViewModel
}