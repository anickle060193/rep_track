package com.adamnickle.reptrack.ui.exercise

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.utils.MutableLiveDataProperty
import javax.inject.Inject

class ExerciseFragmentViewModel @Inject constructor(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    private val exerciseData = MutableLiveData<Exercise?>()

    val exerciseLive: LiveData<Exercise?> get() = exerciseData

    var exercise: Exercise? by MutableLiveDataProperty( exerciseData )

    val exerciseSets: LiveData<List<ExerciseSet>> = Transformations.switchMap( exerciseData ) { exercise ->
        exercise?.id?.let { exerciseId -> workoutDao.getExerciseSetsForExerciseId( exerciseId ) }
    }
}