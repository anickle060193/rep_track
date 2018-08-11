package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.utils.property.MutableLiveDataProperty
import javax.inject.Inject

class UncompletedExerciseSetFragmentViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    private val exerciseSetData = MutableLiveData<ExerciseSet?>()

    val exerciseSetLive: LiveData<ExerciseSet?> get() = exerciseSetData

    var exerciseSet by MutableLiveDataProperty( exerciseSetData )

    val rpe: LiveData<Float?> = Transformations.map( exerciseSetData ) { exerciseSet -> exerciseSet?.rpe }

    val notes: LiveData<String> = Transformations.map( exerciseSetData ) { exerciseSet -> exerciseSet?.notes }

    val accelerometerData: LiveData<List<ExerciseSetAccel>> = Transformations.switchMap( exerciseSetData ) { exerciseSet ->
        exerciseSet?.let { workoutDao.getExerciseSetAccel( exerciseSet.idOrThrow() ) }
    }

    fun bind( exerciseSet: ExerciseSet )
    {
        this.exerciseSet = exerciseSet
    }
}