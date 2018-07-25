package com.adamnickle.reptrack.ui.workout

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class WorkoutViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    fun exercises( workoutId: Long ): LiveData<List<Exercise>> = workoutDao.getExercisesForWorkoutId( workoutId )
}