package com.adamnickle.reptrack.ui.exercise

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class ExerciseFragmentViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    fun exerciseSets( exerciseId: Long ): LiveData<List<ExerciseSet>> = workoutDao.getExerciseSetsForExerciseId( exerciseId )
}