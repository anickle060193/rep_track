package com.adamnickle.reptrack.ui.completedExerciseSet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.utils.AccelerometerParser
import com.adamnickle.reptrack.utils.property.MutableLiveDataProperty
import javax.inject.Inject

class CompletedExerciseSetFragmentViewModel @Inject constructor(): ViewModel()
{
    private val exerciseSetData = MutableLiveData<ExerciseSet>()
    private val accelLiveData = MutableLiveData<AccelerometerParser.AccelData?>()

    val exerciseSetLive: LiveData<ExerciseSet> get() = exerciseSetData

    var exerciseSet by MutableLiveDataProperty( exerciseSetData )

    var accelData: AccelerometerParser.AccelData?
        get() = accelLiveData.value
        set( value ) { accelLiveData.value = value }

    val max: LiveData<Float> = Transformations.map( accelLiveData ) { it?.max ?: 0.0f }
    val min: LiveData<Float> = Transformations.map( accelLiveData ) { it?.min ?: 0.0f }
    val avg: LiveData<Float> = Transformations.map( accelLiveData ) { it?.avg ?: 0.0f }
}
