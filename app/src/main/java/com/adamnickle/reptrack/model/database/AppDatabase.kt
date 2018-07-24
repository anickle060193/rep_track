package com.adamnickle.reptrack.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.adamnickle.reptrack.model.post.Post
import com.adamnickle.reptrack.model.post.PostDao
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao

@Database( entities = [
    Post::class,
    Workout::class,
    Exercise::class,
    ExerciseSet::class
], version = 1, exportSchema = false )
abstract class AppDatabase: RoomDatabase()
{
    abstract fun postDao(): PostDao

    abstract fun workoutDao(): WorkoutDao
}