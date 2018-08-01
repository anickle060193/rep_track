package com.adamnickle.reptrack.injection.module

import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.ui.completedExerciseSet.CompletedExerciseSetFragmentViewModel
import com.adamnickle.reptrack.ui.exercise.ExerciseFragmentViewModel
import com.adamnickle.reptrack.ui.shared.SharedViewModel
import com.adamnickle.reptrack.ui.workout.WorkoutFragmentViewModel
import com.adamnickle.reptrack.ui.workouts.WorkoutListFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress( "unused" )
@Module
abstract class ViewModelModule
{
    @Binds
    @IntoMap
    @ViewModelKey( SharedViewModel::class )
    abstract fun bindSharedViewModel( sharedViewModel: SharedViewModel ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey( WorkoutListFragmentViewModel::class )
    abstract fun bindWorkoutListFragmentViewModel(workoutListFragmentViewModel: WorkoutListFragmentViewModel ): ViewModel

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
    @ViewModelKey( CompletedExerciseSetFragmentViewModel::class )
    abstract fun bindCompletedExerciseSetFragmentViewModel( completedExerciseSetFragmentViewModel: CompletedExerciseSetFragmentViewModel ): ViewModel
}