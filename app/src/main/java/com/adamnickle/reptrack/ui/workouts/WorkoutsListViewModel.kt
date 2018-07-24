package com.adamnickle.reptrack.ui.workouts

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.injection.module.ViewModelKey
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class WorkoutsListViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    val results: LiveData<List<Workout>> = workoutDao.allWorkouts()
}