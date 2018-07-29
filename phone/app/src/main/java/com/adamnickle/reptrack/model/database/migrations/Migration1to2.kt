package com.adamnickle.reptrack.model.database.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

object Migration1to2: Migration( 1, 2 )
{
    override fun migrate( database: SupportSQLiteDatabase )
    {
        database.execSQL( "ALTER TABLE Workout ADD COLUMN deleted INTEGER NOT NULL DEFAULT 0" )
        database.execSQL( "CREATE INDEX index_Workout_deleted ON Workout ( deleted )" )

        database.execSQL( "ALTER TABLE Exercise ADD COLUMN deleted INTEGER NOT NULL DEFAULT 0" )
        database.execSQL( "CREATE INDEX index_Exercise_deleted ON Exercise ( deleted )" )

        database.execSQL( "ALTER TABLE exerciseSet ADD COLUMN deleted INTEGER NOT NULL DEFAULT 0" )
        database.execSQL( "CREATE INDEX index_exerciseSet_deleted ON exerciseSet ( deleted )" )
    }
}