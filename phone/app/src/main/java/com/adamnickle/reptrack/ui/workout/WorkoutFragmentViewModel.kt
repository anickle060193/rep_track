package com.adamnickle.reptrack.ui.workout

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.utils.MutableLiveDataProperty
import javax.inject.Inject

class WorkoutFragmentViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    private var workoutData = MutableLiveData<Workout?>()

    val workoutLive: LiveData<Workout?> get() = workoutData

    var workout by MutableLiveDataProperty( workoutData )

    val exercises: LiveData<List<Exercise>> = Transformations.switchMap( workoutData ) { workout -> workout?.id?.let { workoutId -> workoutDao.getExercisesForWorkoutId( workoutId ) } }
}