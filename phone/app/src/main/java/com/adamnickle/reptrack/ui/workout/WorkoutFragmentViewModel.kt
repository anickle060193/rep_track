package com.adamnickle.reptrack.ui.workout

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class WorkoutFragmentViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    private var workoutIdData = MutableLiveData<Long>()
    private var workoutData = MutableLiveData<Workout>()

    var workout: Workout?
        get() = workoutData.value
        set( value ) { workoutData.value = value }

    var workoutId: Long?
        get() = workoutIdData.value
        set( value ) { workoutIdData.value = value }

    fun exercises(): LiveData<List<Exercise>> = Transformations.switchMap( workoutIdData ) { workoutId -> workoutId?.let { workoutDao.getExercisesForWorkoutId( workoutId ) } }
}