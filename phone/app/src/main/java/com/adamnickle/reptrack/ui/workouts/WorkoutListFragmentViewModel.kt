package com.adamnickle.reptrack.ui.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class WorkoutListFragmentViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    val results: LiveData<List<Workout>> = workoutDao.allWorkouts()
}