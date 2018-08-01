package com.adamnickle.reptrack.ui.workout

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.WorkoutDao

class ExerciseItemViewModel(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    private val exercise = MutableLiveData<Exercise>()
    private val exerciseSetCount = Transformations.switchMap( exercise ) { exercise ->
        workoutDao.getExerciseSetCountForExerciseId( exercise.id ?: throw IllegalArgumentException( "Cannot display unsaved Exercise" ) )
    }
    private val exerciseSetCountString = Transformations.map( exerciseSetCount ) { exerciseSetCount -> "$exerciseSetCount sets" }

    fun bind( exercise: Exercise )
    {
        this.exercise.value = exercise
    }

    fun getExerciseSetCount() = exerciseSetCountString!!
}