package com.adamnickle.reptrack.ui.exercise

import androidx.lifecycle.*
import com.adamnickle.reptrack.model.workout.ExerciseSet

class ExerciseSetItemViewModel: ViewModel()
{
    val exerciseSet = MutableLiveData<ExerciseSet>()

    val exerciseSetNumber = MutableLiveData<Int>()

    val description = MediatorLiveData<String>()
    init
    {
        this.description.addSource( exerciseSet ) { exerciseSet -> this.description.value = formatDescription( exerciseSet, exerciseSetNumber.value ) }
        this.description.addSource( exerciseSetNumber ) { exerciseSetNumber -> this.description.value = formatDescription( exerciseSet.value, exerciseSetNumber ) }
    }

    val completed: LiveData<Boolean> = Transformations.map( exerciseSet ) { it.completed }

    fun bind( exerciseSet: ExerciseSet, exerciseSetNumber: Int )
    {
        this.exerciseSet.value = exerciseSet
        this.exerciseSetNumber.value = exerciseSetNumber
    }

    private fun formatDescription( exerciseSet: ExerciseSet?, exerciseSetNumber: Int? ): String
    {
        return "Set ${exerciseSetNumber ?: -1}: ${exerciseSet?.repCount ?: -1} @ ${exerciseSet?.weight?.toInt() ?: -1} lbs"
    }
}