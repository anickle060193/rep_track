package com.adamnickle.reptrack.model.database

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.adamnickle.reptrack.model.database.migrations.Migration2to3
import org.junit.Test
import org.junit.runner.RunWith

@RunWith( AndroidJUnit4::class )
class Migration2to3Test
{
    companion object
    {
        const val TEST_DB = "migration-2-3-test"
    }

    private val helper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            AppDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory())

    @Test
    fun migrate()
    {
        val db = helper.createDatabase( TEST_DB, 2 )

        db.close()

        helper.runMigrationsAndValidate( TEST_DB, 3, true, Migration2to3 )
    }
}