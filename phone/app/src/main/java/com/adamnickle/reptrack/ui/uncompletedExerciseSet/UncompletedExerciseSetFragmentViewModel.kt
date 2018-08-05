package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet
import javax.inject.Inject

class UncompletedExerciseSetFragmentViewModel @Inject constructor(): ViewModel()
{
    private val exerciseSet = MutableLiveData<ExerciseSet>()

    fun bind( exerciseSet: ExerciseSet)
    {
        this.exerciseSet.value = exerciseSet
    }
}