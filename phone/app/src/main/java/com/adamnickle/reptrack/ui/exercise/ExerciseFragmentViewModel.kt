package com.adamnickle.reptrack.ui.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class ExerciseFragmentViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    val exercise = MutableLiveData<Exercise?>()

    val exerciseSets: LiveData<List<ExerciseSet>> = Transformations.switchMap( exercise ) { exercise ->
        exercise?.let { workoutDao.getExerciseSetsForExerciseId( exercise.idOrThrow() ) }
    }
}