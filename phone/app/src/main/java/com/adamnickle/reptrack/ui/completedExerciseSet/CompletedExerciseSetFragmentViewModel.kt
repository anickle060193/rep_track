package com.adamnickle.reptrack.ui.completedExerciseSet

import android.arch.lifecycle.*
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.utils.AccelerometerParser
import javax.inject.Inject

class CompletedExerciseSetFragmentViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    val exerciseSet = MutableLiveData<ExerciseSet>()
    val selectedExerciseSetRep = MutableLiveData<Int>()
    init
    {
        selectedExerciseSetRep.value = 0
    }

    val exerciseSetAccels: LiveData<List<ExerciseSetAccel>> = Transformations.switchMap( exerciseSet ) { exerciseSet ->
        exerciseSet?.let {
            workoutDao.getExerciseSetAccel( exerciseSet.idOrThrow() )
        }
    }

    val selectedExerciseSetRepAccels = MediatorLiveData<List<ExerciseSetAccel>>()
    init
    {
        selectedExerciseSetRepAccels.addSource( exerciseSetAccels ) { accels ->
            selectedExerciseSetRepAccels.value = getRepAccels( accels, exerciseSet.value?.repCount, selectedExerciseSetRep.value )
        }

        selectedExerciseSetRepAccels.addSource( exerciseSet ) { exerciseSet ->
            selectedExerciseSetRepAccels.value = getRepAccels( exerciseSetAccels.value, exerciseSet?.repCount, selectedExerciseSetRep.value )
        }

        selectedExerciseSetRepAccels.addSource( selectedExerciseSetRep ) { selectedExerciseSetRep ->
            selectedExerciseSetRepAccels.value = getRepAccels( exerciseSetAccels.value, exerciseSet.value?.repCount, selectedExerciseSetRep )
        }
    }

    val selectedExerciseSetAccelData: LiveData<AccelerometerParser.AccelData> = Transformations.map( selectedExerciseSetRepAccels ) { accels ->
        accels?.let {
            AccelerometerParser.getAccelerationData( accels )
        }
    }

    val selectedExerciseSetMax: LiveData<Float> = Transformations.map( selectedExerciseSetAccelData ) { it?.max ?: 0.0f }
    val selectedExerciseSetMin: LiveData<Float> = Transformations.map( selectedExerciseSetAccelData ) { it?.min ?: 0.0f }
    val selectedExerciseSetAvg: LiveData<Float> = Transformations.map( selectedExerciseSetAccelData ) { it?.avg ?: 0.0f }

    private fun getRepAccels( accels: List<ExerciseSetAccel>?, repCount: Int?, selectedRep: Int? ): List<ExerciseSetAccel>?
    {
        if( accels == null
         || repCount == null
         || selectedRep == null )
        {
            return null
        }

        if( selectedRep == 0 )
        {
            return accels
        }
        else
        {
            val repAccels = AccelerometerParser.findRepsAccels( accels, repCount )
            if( selectedRep - 1 < repAccels.size )
            {
                return repAccels[ selectedRep - 1 ]
            }
            else
            {
                throw IllegalArgumentException( "Selected Rep is outside of valid range - selectedRep: $selectedRep - repCount: $repCount" )
            }
        }
    }
}
