package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.model.workout.WorkoutDao
import javax.inject.Inject

class UncompletedExerciseSetFragmentViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    private val exerciseSet = MutableLiveData<ExerciseSet?>()

    val accelerometerData: LiveData<List<ExerciseSetAccel>> = Transformations.switchMap( exerciseSet ) { exerciseSet ->
        exerciseSet?.id?.let { exerciseSetId -> workoutDao.getExerciseSetAccel( exerciseSetId ) }
    }

    fun bind( exerciseSet: ExerciseSet )
    {
        this.exerciseSet.value = exerciseSet
    }
}