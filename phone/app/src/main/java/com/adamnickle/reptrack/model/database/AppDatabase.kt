package com.adamnickle.reptrack.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adamnickle.reptrack.model.workout.*

@Database( entities = [
    Workout::class,
    Exercise::class,
    ExerciseSet::class,
    ExerciseSetAccel::class
], version = 4, exportSchema = true )
abstract class AppDatabase: RoomDatabase()
{
    abstract fun workoutDao(): WorkoutDao
}