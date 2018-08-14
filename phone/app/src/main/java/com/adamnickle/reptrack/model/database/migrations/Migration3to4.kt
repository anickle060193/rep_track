package com.adamnickle.reptrack.model.database.migrations

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

object Migration3to4: Migration( 3, 4 )
{
    override fun migrate( database: SupportSQLiteDatabase)
    {
        database.execSQL( "ALTER TABLE exerciseSet ADD COLUMN rpe REAL" )
        database.execSQL( "ALTER TABLE exerciseSet ADD COLUMN notes TEXT NOT NULL DEFAULT ''" )
    }
}