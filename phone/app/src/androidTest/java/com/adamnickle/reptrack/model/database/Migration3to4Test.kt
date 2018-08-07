package com.adamnickle.reptrack.model.database

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.adamnickle.reptrack.model.database.migrations.Migration3to4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith( AndroidJUnit4::class )
class Migration3to4Test
{
    companion object
    {
        const val TEST_DB = "migration-3-4-test"

        data class ExerciseSet3(var id: Long, var completed: Boolean, var weight: Float, var repCount: Int, var exerciseId: Long, var order: Int, var deleted: Boolean )

        val EXERCISE_SETS_3 = arrayOf(
                ExerciseSet3( 0, false, 1.0f, 0, 0, 0, false ),
                ExerciseSet3( 1, true, 123.0f, 100, 1, 0, false ),
                ExerciseSet3( 2, false, 45.0f, 20, 2, 0, false ),
                ExerciseSet3( 3, true, 9999.0f, 9, 3, 0, false )
        )
    }

    private val helper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory())

    @Test
    fun migrate()
    {
        var db = helper.createDatabase( TEST_DB, 3 )

        for( exerciseSet in EXERCISE_SETS_3 )
        {
            val values = ContentValues()
            values.put( "id", exerciseSet.id )
            values.put( "completed", exerciseSet.completed )
            values.put( "weight", exerciseSet.weight )
            values.put( "repCount", exerciseSet.repCount )
            values.put( "exerciseId", exerciseSet.exerciseId )
            values.put( "'order'", exerciseSet.order )
            values.put( "deleted", exerciseSet.deleted )
            db.insert( "exerciseSet", SQLiteDatabase.CONFLICT_REPLACE, values )
        }

        db.close()

        db = helper.runMigrationsAndValidate( TEST_DB, 4, true, Migration3to4 )

        val result = db.query( "SELECT * FROM exerciseSet" )

        assertEquals( result.count, EXERCISE_SETS_3.size )

        for( exerciseSet in EXERCISE_SETS_3 )
        {
            result.moveToNext()

            assertEquals( result.getLong( result.getColumnIndex( "id" ) ), exerciseSet.id )
            assertEquals( result.getInt( result.getColumnIndex( "completed" ) ), if( exerciseSet.completed ) 1 else 0 )
            assertEquals( result.getFloat( result.getColumnIndex( "weight" ) ), exerciseSet.weight )
            assertEquals( result.getInt( result.getColumnIndex( "repCount" ) ), exerciseSet.repCount )
            assertEquals( result.getLong( result.getColumnIndex( "exerciseId" ) ), exerciseSet.exerciseId )
            assertEquals( result.getInt( result.getColumnIndex( "order" ) ), exerciseSet.order )
            assertEquals( result.getInt( result.getColumnIndex( "deleted" ) ), 0 )

            assertTrue( result.isNull( result.getColumnIndex( "rep" ) ) )
            assertEquals( result.getString( result.getColumnIndex( "notes" ) ), "" )
        }
    }
}