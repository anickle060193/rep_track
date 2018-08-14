package com.adamnickle.reptrack.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class WorkoutFragmentViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    val workout = MutableLiveData<Workout?>()

    val exercises: LiveData<List<Exercise>> = Transformations.switchMap( workout ) { workout ->
        workout?.let { workoutDao.getExercisesForWorkoutId( workout.idOrThrow() ) }
    }
}