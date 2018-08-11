package com.adamnickle.reptrack.ui.workout

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.WorkoutDao

class ExerciseItemViewModel(
        private val workoutDao: WorkoutDao
): ViewModel()
{
    val exercise = MutableLiveData<Exercise?>()

    val exerciseName: LiveData<String> = Transformations.map( exercise ) { exercise -> exercise?.name ?: "" }

    val exerciseSetCount: LiveData<Int> = Transformations.switchMap( exercise ) { exercise ->
        exercise?.let {
            workoutDao.getExerciseSetCountForExerciseId( exercise.idOrThrow() )
        }
    }

    val exerciseSetCountDisplay: LiveData<String> = Transformations.map( exerciseSetCount ) { exerciseSetCount ->
        "${exerciseSetCount ?: 0} sets"
    }

    fun bind( exercise: Exercise )
    {
        this.exercise.value = exercise
    }
}