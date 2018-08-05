package com.adamnickle.reptrack.injection.module

import android.arch.persistence.room.Room
import com.adamnickle.reptrack.RepTrackApp
import com.adamnickle.reptrack.model.database.AppDatabase
import com.adamnickle.reptrack.model.database.migrations.Migration1to2
import com.adamnickle.reptrack.model.database.migrations.Migration2to3
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
            .addMigrations( Migration1to2, Migration2to3 )
            .build()

    @Provides
    @Singleton
    fun provideWorkoutDao( database: AppDatabase ): WorkoutDao = database.workoutDao()
}