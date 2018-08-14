package com.adamnickle.reptrack.model.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

object Migration2to3: Migration( 2, 3 )
{
    override fun migrate( database: SupportSQLiteDatabase)
    {
        database.execSQL( "CREATE TABLE ExerciseSetAccel ( id INTEGER PRIMARY KEY AUTOINCREMENT, x REAL NOT NULL, y REAL NOT NULL, z REAL NOT NULL, time INTEGER NOT NULL, exerciseSetId INTEGER NOT NULL REFERENCES exerciseSet( id ) ON UPDATE CASCADE ON DELETE CASCADE )" )
        database.execSQL( "CREATE INDEX index_ExerciseSetAccel_time ON ExerciseSetAccel( time )" )
        database.execSQL( "CREATE UNIQUE INDEX index_ExerciseSetAccel_exerciseSetId_time ON ExerciseSetAccel( exerciseSetId, time )" )
    }
}