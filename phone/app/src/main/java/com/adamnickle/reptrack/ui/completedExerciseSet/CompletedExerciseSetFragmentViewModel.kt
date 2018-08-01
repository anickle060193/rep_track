package com.adamnickle.reptrack.ui.completedExerciseSet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import javax.inject.Inject

class CompletedExerciseSetFragmentViewModel @Inject constructor(): ViewModel()
{
    private val exerciseSet = MutableLiveData<ExerciseSet>()

    var repMin = 0.0f
    var repMax = 0.0f
    var repAvg = 0.0f

    fun bind( exerciseSet: ExerciseSet )
    {
        this.exerciseSet.value = exerciseSet
    }
}
