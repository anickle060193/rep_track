package com.adamnickle.reptrack.model.workout

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class WorkoutDao
{
    @Query( "SELECT * FROM workout WHERE deleted = 0" )
    abstract fun allWorkouts(): LiveData<List<Workout>>

    @Query( "SELECT * FROM workout WHERE id = :workoutId LIMIT 1" )
    abstract fun getWorkout( workoutId: Long ): Workout?

    @Ignore
    fun getWorkoutOrThrow( workoutId: Long ): Workout
    {
        return getWorkout( workoutId ) ?: throw IllegalArgumentException( "Could not find Workout: $workoutId" )
    }

    @SuppressWarnings( RoomWarnings.CURSOR_MISMATCH )
    @Transaction
    @Query( "SELECT * FROM workout WHERE id = :workoutId LIMIT 1" )
    abstract fun getFullWorkout( workoutId: Long ): FullWorkout?

    @Insert
    abstract fun insertWorkout( workout: Workout ): Long

    @Update
    abstract fun updateWorkout( workout: Workout )

    @Delete
    abstract fun deleteWorkout( workout: Workout )

    fun markWorkoutAsDeleted( workout: Workout )
    {
        workout.deleted = true
        updateWorkout( workout )
    }

    fun unmarkWorkoutAsDeleted( workout: Workout )
    {
        workout.deleted = false
        updateWorkout( workout )
    }

    @Query( "SELECT * FROM Exercise WHERE id = :exerciseId LIMIT 1" )
    abstract fun getExercise( exerciseId: Long ): Exercise?

    @Ignore
    fun getExerciseOrThrow( exerciseId: Long ): Exercise
    {
        return getExercise( exerciseId ) ?: throw IllegalArgumentException( "Could not find Exercise: $exerciseId" )
    }

    @Insert
    abstract fun insertExercise( exercise: Exercise ): Long

    @Update
    abstract fun updateExercise( exercise: Exercise )

    @Delete
    abstract fun deleteExercise( exercise: Exercise )

    fun markExerciseAsDeleted( exercise: Exercise )
    {
        exercise.deleted = true
        updateExercise( exercise )
    }

    fun unmarkExerciseAsDeleted( exercise: Exercise )
    {
        exercise.deleted = false
        updateExercise( exercise )
    }

    @Query( "SELECT * FROM exercise WHERE workoutId = :workoutId AND deleted = 0" )
    abstract fun getExercisesForWorkoutId( workoutId: Long ): LiveData<List<Exercise>>

    @Query( "SELECT MAX( `order` ) + 1 FROM exercise WHERE workoutId = :workoutId ")
    abstract fun getNextExerciseOrderForWorkoutId( workoutId: Long ): Int

    @Transaction
    open fun swapExercisesInWorkout( a: Exercise, b: Exercise )
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

    @Query( "SELECT * FROM exerciseSet WHERE id = :exerciseSetId LIMIT 1" )
    abstract fun getExerciseSet( exerciseSetId: Long ): ExerciseSet?

    @Ignore
    fun getExerciseSetOrThrow( exerciseSetId: Long ): ExerciseSet
    {
        return getExerciseSet( exerciseSetId ) ?: throw IllegalArgumentException( "Could not find Exercise Set: $exerciseSetId" )
    }

    @Insert
    abstract fun insertExerciseSet( exerciseSet: ExerciseSet ): Long

    @Insert
    abstract fun insertExerciseSets( exerciseSets: List<ExerciseSet> )

    @Update
    abstract fun updateExerciseSet( exerciseSet: ExerciseSet )

    @Delete
    abstract fun deleteExerciseSet( exerciseSet: ExerciseSet )

    fun markExerciseSetAsDeleted( exerciseSet: ExerciseSet )
    {
        exerciseSet.deleted = true
        updateExerciseSet( exerciseSet )
    }

    fun unmarkExerciseSetAsDeleted( exerciseSet: ExerciseSet )
    {
        exerciseSet.deleted = false
        updateExerciseSet( exerciseSet )
    }

    @Query( "SELECT * FROM exerciseSet WHERE exerciseId = :exerciseId AND deleted = 0" )
    abstract fun getExerciseSetsForExerciseId( exerciseId: Long ): LiveData<List<ExerciseSet>>

    @Query( "SELECT COUNT( * ) FROM exerciseSet WHERE exerciseId = :exerciseId AND deleted = 0" )
    abstract fun getExerciseSetCountForExerciseId( exerciseId: Long ): LiveData<Int>

    @Transaction
    open fun swapExerciseSetsInExercise( a: ExerciseSet, b: ExerciseSet )
    {
        if( a.id == b.id )
        {
            throw IllegalArgumentException( "Attempting to swap Exercise Set with same Exercise Set." )
        }
        if( a.exerciseId != b.exerciseId )
        {
            throw IllegalArgumentException( "Exercise Sets do not belong to same Exercise." )
        }

        val aOrder = a.order
        val bOrder = b.order

        a.order = Int.MIN_VALUE
        updateExerciseSet( a )

        b.order = aOrder
        updateExerciseSet( b )

        a.order = bOrder
        updateExerciseSet( a )
    }

    fun markExerciseSetCompleted( exerciseSet: ExerciseSet )
    {
        exerciseSet.completed = true
        updateExerciseSet( exerciseSet )
    }

    fun unmarkExerciseSetCompleted( exerciseSet: ExerciseSet )
    {
        exerciseSet.completed = false
        updateExerciseSet( exerciseSet )
    }

    @Insert
    abstract fun insertExerciseSetAccel( exerciseSetAccel: List<ExerciseSetAccel> )

    @Query( "SELECT * FROM ExerciseSetAccel WHERE exerciseSetId = :exerciseSetId" )
    abstract fun getExerciseSetAccel( exerciseSetId: Long ): LiveData<List<ExerciseSetAccel>>

    @Query( "DELETE FROM ExerciseSetAccel WHERE exerciseSetId = :exerciseSetId AND time > :time" )
    abstract fun deleteExerciseSetAccelAfter( exerciseSetId: Long, time: Long )

    @Query( "DELETE FROM ExerciseSetAccel WHERE exerciseSetId = :exerciseSetId AND time < :time" )
    abstract fun deleteExerciseSetAccelBefore( exerciseSetId: Long, time: Long )
}