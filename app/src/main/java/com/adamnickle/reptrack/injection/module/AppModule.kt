package com.adamnickle.reptrack.injection.module

import android.arch.persistence.room.Room
import com.adamnickle.reptrack.RepTrackApp
import com.adamnickle.reptrack.model.database.AppDatabase
import com.adamnickle.reptrack.model.workout.WorkoutDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule
{
    @Provides
    @Singleton
    fun provideDatabase( app: RepTrackApp ): AppDatabase = Room
            .databaseBuilder( app, AppDatabase::class.java, "rep_track.db" )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideWorkoutDao( database: AppDatabase ): WorkoutDao = database.workoutDao()
}