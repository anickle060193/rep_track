package com.adamnickle.reptrack.ui.completedExerciseSet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.utils.property.MutableLiveDataProperty
import javax.inject.Inject

class CompletedExerciseSetFragmentViewModel @Inject constructor(): ViewModel()
{
    private val exerciseSetData = MutableLiveData<ExerciseSet>()

    val exerciseSetLive: LiveData<ExerciseSet> get() = exerciseSetData

    var exerciseSet by MutableLiveDataProperty( exerciseSetData )

    var repMin = 0.0f
    var repMax = 0.0f
    var repAvg = 0.0f
}
