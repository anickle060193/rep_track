package com.adamnickle.reptrack.model.workout

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface WorkoutDao
{
    @Query( "SELECT * FROM workout" )
    fun allWorkouts(): LiveData<List<Workout>>

    @Insert
    fun insertWorkout( workout: Workout )

    @Update
    fun updateWorkout( workout: Workout )

    @Delete
    fun deleteWorkout( workout: Workout )

    fun getExercisesForWorkout( workout: Workout ) = workout.id?.let{ getExercisesForWorkoutId( it ) }

    @Insert
    fun insertExercise( exercise: Exercise )

    @Update
    fun updateExercise( exercise: Exercise )

    @Delete
    fun deleteExercise( exercise: Exercise )

    @Query( "SELECT * FROM exercise WHERE workoutId = :workoutId" )
    fun getExercisesForWorkoutId( workoutId: Long ): LiveData<List<Exercise>>

    @Insert
    fun insertExerciseSet( exerciseSet: ExerciseSet )

    @Update
    fun updateExerciseSet( exerciseSet: ExerciseSet )

    @Delete
    fun deleteExerciseSet( exerciseSet: ExerciseSet )

    fun getExerciseSetsForExercise( exercise: Exercise ) = exercise.id?.let { getExerciseSetsForExerciseId( it ) }

    @Query( "SELECT * FROM exerciseSet WHERE exerciseId = :exerciseId" )
    fun getExerciseSetsForExerciseId( exerciseId: Long ): LiveData<List<ExerciseSet>>
}