package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.utils.AccelerometerParser
import javax.inject.Inject

class UncompletedExerciseSetFragmentViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    val exerciseSet = MutableLiveData<ExerciseSet>()

    val rpe: LiveData<Float?> = Transformations.map( exerciseSet ) { exerciseSet -> exerciseSet?.rpe }

    val notes: LiveData<String> = Transformations.map( exerciseSet ) { exerciseSet -> exerciseSet?.notes }

    val accelerometerData: LiveData<List<ExerciseSetAccel>> = Transformations.switchMap( exerciseSet ) { exerciseSet ->
        exerciseSet?.let { workoutDao.getExerciseSetAccel( exerciseSet.idOrThrow() ) }
    }

    val combinedAccelerometerData: LiveData<List<AccelerometerParser.CombinedAccel>> = Transformations.map( accelerometerData ) { accelerometerData ->
        AccelerometerParser.getCombinedAccels( accelerometerData )
    }

    fun bind( exerciseSet: ExerciseSet )
    {
        this.exerciseSet.value = exerciseSet
    }
}