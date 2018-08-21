package com.adamnickle.reptrack.ui.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.utils.extensions.toShortString

class WorkoutItemViewModel: ViewModel()
{
    val workout = MutableLiveData<Workout>()

    val workoutName: LiveData<String> = Transformations.map( workout ) { it.name }

    val workoutDate: LiveData<String> = Transformations.map( workout ) { it.date.toShortString() }
}