package com.adamnickle.reptrack.model.database

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.adamnickle.reptrack.model.database.migrations.Migration1to2
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith( AndroidJUnit4::class )
class Migration1to2Test
{
    companion object
    {
        const val TEST_DB = "migration-1-2-test"

        data class Workout1( val id: Long, val name: String, val date: Int )

        val WORKOUTS_1 = arrayOf(
                Workout1( 0, "7/12/2018", 7122018 ),
                Workout1( 1, "8/25/2017", 8252017 ),
                Workout1( 3, "6/15/1998", 6151998 )
        )

        data class Exercise1( var id: Long, var name: String, var workoutId: Long, var order: Int )

        val EXERCISES_1 = arrayOf(
                Exercise1( 0, "Squat", 0, 0 ),
                Exercise1( 1, "Deadlift", 1, 0 ),
                Exercise1( 2, "Clean", 2, 0 ),
                Exercise1( 3, "Bench", 3, 0 )
        )

        data class ExerciseSet1( var id: Long, var completed: Boolean, var weight: Float, var repCount: Int, var exerciseId: Long, var order: Int )

        val EXERCISE_SETS_1 = arrayOf(
                ExerciseSet1( 0, false, 1.0f, 0, 0, 0 ),
                ExerciseSet1( 1, true, 123.0f, 100, 1, 0 ),
                ExerciseSet1( 2, false, 45.0f, 20, 2, 0 ),
                ExerciseSet1( 3, true, 9999.0f, 9, 3, 0 )
        )
    }

    private val helper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory() )

    @Test
    fun migrateWorkouts()
    {
        var db = helper.createDatabase( TEST_DB, 1 )

        for( workout in WORKOUTS_1 )
        {
            val values = ContentValues()
            values.put( "id", workout.id )
            values.put( "name", workout.name )
            values.put( "date", workout.date )
            db.insert( "Workout", SQLiteDatabase.CONFLICT_REPLACE, values )
        }

        db.close()

        db = helper.runMigrationsAndValidate( TEST_DB, 2, true, Migration1to2 )

        val result = db.query( "SELECT * FROM Workout" )

        assertEquals( WORKOUTS_1.size, result.count )

        for( workout in WORKOUTS_1 )
        {
            result.moveToNext()

            assertEquals( result.getLong( result.getColumnIndex( "id" ) ), workout.id )
            assertEquals( result.getString( result.getColumnIndex( "name" ) ), workout.name )
            assertEquals( result.getInt( result.getColumnIndex( "date" ) ), workout.date )

            assertEquals( result.getInt( result.getColumnIndex( "deleted" ) ), 0 )
        }
    }

    @Test
    fun migrateExercises()
    {
        var db = helper.createDatabase( TEST_DB, 1 )

        for( exercise in EXERCISES_1 )
        {
            val values = ContentValues()
            values.put( "id", exercise.id )
            values.put( "name", exercise.name )
            values.put( "workoutId", exercise.workoutId )
            values.put( "'order'", exercise.order )
            db.insert( "Exercise", SQLiteDatabase.CONFLICT_REPLACE, values )
        }

        db.close()

        db = helper.runMigrationsAndValidate( TEST_DB, 2, true, Migration1to2 )

        val result = db.query( "SELECT * FROM Exercise" )

        assertEquals( result.count, EXERCISES_1.size )

        for( exercise in EXERCISES_1 )
        {
            result.moveToNext()

            assertEquals( result.getLong( result.getColumnIndex( "id" ) ), exercise.id )
            assertEquals( result.getString( result.getColumnIndex( "name" ) ), exercise.name )
            assertEquals( result.getLong( result.getColumnIndex( "workoutId" ) ), exercise.workoutId )
            assertEquals( result.getInt( result.getColumnIndex( "order" ) ), exercise.order )

            assertEquals( result.getInt( result.getColumnIndex( "deleted" ) ), 0 )
        }
    }

    @Test
    fun migrateExerciseSets()
    {
        var db = helper.createDatabase( TEST_DB, 1 )

        for( exerciseSet in EXERCISE_SETS_1 )
        {
            val values = ContentValues()
            values.put( "id", exerciseSet.id )
            values.put( "completed", exerciseSet.completed )
            values.put( "weight", exerciseSet.weight )
            values.put( "repCount", exerciseSet.repCount )
            values.put( "exerciseId", exerciseSet.exerciseId )
            values.put( "'order'", exerciseSet.order )
            db.insert( "exerciseSet", SQLiteDatabase.CONFLICT_REPLACE, values )
        }

        db.close()

        db = helper.runMigrationsAndValidate( TEST_DB, 2, true, Migration1to2 )

        val result = db.query( "SELECT * FROM exerciseSet" )

        assertEquals( result.count, EXERCISE_SETS_1.size )

        for( exerciseSet in EXERCISE_SETS_1 )
        {
            result.moveToNext()

            assertEquals( result.getLong( result.getColumnIndex( "id" ) ), exerciseSet.id )
            assertEquals( result.getInt( result.getColumnIndex( "completed" ) ), if( exerciseSet.completed ) 1 else 0 )
            assertEquals( result.getFloat( result.getColumnIndex( "weight" ) ), exerciseSet.weight )
            assertEquals( result.getInt( result.getColumnIndex( "repCount" ) ), exerciseSet.repCount )
            assertEquals( result.getLong( result.getColumnIndex( "exerciseId" ) ), exerciseSet.exerciseId )
            assertEquals( result.getInt( result.getColumnIndex( "order" ) ), exerciseSet.order )

            assertEquals( result.getInt( result.getColumnIndex( "deleted" ) ), 0 )
        }
    }
}