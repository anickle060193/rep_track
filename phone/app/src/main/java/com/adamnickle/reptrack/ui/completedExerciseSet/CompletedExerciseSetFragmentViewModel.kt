package com.adamnickle.reptrack.ui.completedExerciseSet

import androidx.lifecycle.*
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.utils.AccelerometerParser
import com.adamnickle.reptrack.utils.Convert
import javax.inject.Inject

class CompletedExerciseSetFragmentViewModel @Inject constructor(
        workoutDao: WorkoutDao
): ViewModel()
{
    val exerciseSet = MutableLiveData<ExerciseSet>()

    val rpe: LiveData<Float> = Transformations.map( exerciseSet ) { it.rpe }

    val notes: LiveData<String> = Transformations.map( exerciseSet ) { it.notes }

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

    val exerciseSetRepAccels = MediatorLiveData<List<List<ExerciseSetAccel>>>()
    init
    {
        exerciseSetRepAccels.addSource( exerciseSetAccels ) { accels ->
            exerciseSetRepAccels.value = getRepAccels( accels, exerciseSet.value?.repCount )
        }

        exerciseSetRepAccels.addSource( exerciseSet ) { exerciseSet ->
            exerciseSetRepAccels.value = getRepAccels( exerciseSetAccels.value, exerciseSet?.repCount )
        }
    }

    val selectedExerciseSetRepAccels = MediatorLiveData<List<ExerciseSetAccel>>()
    init
    {
        selectedExerciseSetRepAccels.addSource( exerciseSetRepAccels ) { repAccels ->
            selectedExerciseSetRepAccels.value = getSelectedRepAccels( exerciseSetAccels.value, repAccels, selectedExerciseSetRep.value )
        }

        selectedExerciseSetRepAccels.addSource( selectedExerciseSetRep ) { selectedExerciseSetRep ->
            selectedExerciseSetRepAccels.value = getSelectedRepAccels( exerciseSetAccels.value, exerciseSetRepAccels.value, selectedExerciseSetRep )
        }
    }

    val selectedCombinedExerciseSetRepAccels: LiveData<List<AccelerometerParser.CombinedAccel>> = Transformations.map( selectedExerciseSetRepAccels ) { selectedExerciseSetRepAccels ->
        selectedExerciseSetRepAccels?.let {
            AccelerometerParser.getCombinedAccels( selectedExerciseSetRepAccels )
        }
    }

    val selectedExerciseSetAccelData: LiveData<AccelerometerParser.AccelData> = Transformations.map( selectedExerciseSetRepAccels ) { accels ->
        accels?.let {
            AccelerometerParser.getAccelerationData( accels )
        }
    }

    val selectedExerciseSetMax: LiveData<Float> = Transformations.map( selectedExerciseSetAccelData ) { Convert.mGtoMPS( it?.max ?: 0.0f ) }
    val selectedExerciseSetMin: LiveData<Float> = Transformations.map( selectedExerciseSetAccelData ) { Convert.mGtoMPS( it?.min ?: 0.0f ) }
    val selectedExerciseSetAvg: LiveData<Float> = Transformations.map( selectedExerciseSetAccelData ) { Convert.mGtoMPS( it?.avg ?: 0.0f ) }

    private fun getRepAccels( accels: List<ExerciseSetAccel>?, repCount: Int? ): List<List<ExerciseSetAccel>>?
    {
        if( accels == null
         || repCount == null )
        {
            return null
        }

        return AccelerometerParser.findRepsAccels( accels, repCount )
    }

    private fun getSelectedRepAccels( accels: List<ExerciseSetAccel>?, repAccels: List<List<ExerciseSetAccel>>?, selectedRep: Int? ): List<ExerciseSetAccel>?
    {
        if( repAccels == null
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
            if( selectedRep - 1 < repAccels.size )
            {
                return repAccels[ selectedRep - 1 ]
            }
            else
            {
                throw IllegalArgumentException( "Selected Rep is outside of valid range - selectedRep: $selectedRep - repCount: ${repAccels.size}" )
            }
        }
    }
}
