package com.adamnickle.reptrack.model.workout

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface WorkoutDao
{
    @Query( "SELECT * FROM workout" )
    fun allWorkouts(): LiveData<List<Workout>>

    @Query( "SELECT * FROM workout WHERE id = :workoutId LIMIT 1" )
    fun getWorkout( workoutId: Long ): Workout

    @Insert
    fun insertWorkout( workout: Workout ): Long

    @Update
    fun updateWorkout( workout: Workout )

    @Delete
    fun deleteWorkout( workout: Workout )

    @Insert
    fun insertExercise( exercise: Exercise ): Long

    @Update
    fun updateExercise( exercise: Exercise )

    @Delete
    fun deleteExercise( exercise: Exercise )

    @Query( "SELECT * FROM exercise WHERE workoutId = :workoutId" )
    fun getExercisesForWorkoutId( workoutId: Long ): LiveData<List<Exercise>>

    @Query( "SELECT MAX( `order` ) + 1 FROM exercise WHERE workoutId = :workoutId ")
    fun getNextExerciseOrderForWorkoutId( workoutId: Long ): Int

    @Transaction
    fun swapExercisesInWorkout( a: Exercise, b: Exercise )
    {
        if( a.id == b.id )
        {
            throw IllegalArgumentException( "Attempting to swap Exercise with same Exercise." )
        }
        if( a.workoutId != b.workoutId )
        {
            throw IllegalArgumentException( "Exercises do not belong to same Workout." )
        }

        val aOrder = a.order
        val bOrder = b.order

        a.order = Int.MIN_VALUE
        updateExercise( a )

        b.order = aOrder
        updateExercise( b )

        a.order = bOrder
        updateExercise( a )
    }

    @Insert
    fun insertExerciseSet( exerciseSet: ExerciseSet ): Long

    @Insert
    fun insertExerciseSets( exerciseSets: List<ExerciseSet> )

    @Update
    fun updateExerciseSet( exerciseSet: ExerciseSet )

    @Delete
    fun deleteExerciseSet( exerciseSet: ExerciseSet )

    @Query( "SELECT * FROM exerciseSet WHERE exerciseId = :exerciseId" )
    fun getExerciseSetsForExerciseId( exerciseId: Long ): LiveData<List<ExerciseSet>>

    @Query( "SELECT COUNT( * ) FROM exerciseSet WHERE exerciseId = :exerciseId" )
    fun getExerciseSetCountForExerciseId( exerciseId: Long ): LiveData<Int>
}