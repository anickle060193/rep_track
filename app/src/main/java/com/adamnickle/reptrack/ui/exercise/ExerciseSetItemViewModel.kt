package com.adamnickle.reptrack.ui.exercise

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.ExerciseSet

class ExerciseSetItemViewModel: ViewModel()
{
    private val exerciseSetData = MutableLiveData<ExerciseSet>()
    private val exerciseSetNumberData = MutableLiveData<Int>()
    private val descriptionData = MediatorLiveData<String>()

    init
    {
        descriptionData.addSource( exerciseSetData ) { exerciseSet -> descriptionData.value = formatDescription( exerciseSet, exerciseSetNumberData.value ) }
        descriptionData.addSource( exerciseSetNumberData ) { exerciseSetNumber -> descriptionData.value = formatDescription( exerciseSetData.value, exerciseSetNumber ) }
    }

    val exerciseSet get() = exerciseSetData.value

    val description get() = descriptionData

    fun bind( exerciseSet: ExerciseSet, exerciseSetNumber: Int )
    {
        exerciseSetData.value = exerciseSet
        exerciseSetNumberData.value = exerciseSetNumber
    }

    private fun formatDescription( exerciseSet: ExerciseSet?, exerciseSetNumber: Int? ): String
    {
        return "Set ${exerciseSetNumber ?: -1}: ${exerciseSet?.repCount ?: -1} @ ${exerciseSet?.weight?.toInt() ?: -1} lbs"
    }
}