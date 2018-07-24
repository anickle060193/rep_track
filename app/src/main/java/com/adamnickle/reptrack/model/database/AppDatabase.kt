package com.adamnickle.reptrack.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao

@Database( entities = [
    Workout::class,
    Exercise::class,
    ExerciseSet::class
], version = 2, exportSchema = false )
abstract class AppDatabase: RoomDatabase()
{
    abstract fun workoutDao(): WorkoutDao
}