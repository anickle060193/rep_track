package com.adamnickle.reptrack

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppExecutors(
        private val diskIO: Executor,
        private val mainThread: Executor
)
{
    @Inject
    constructor() : this(
            Executors.newSingleThreadExecutor(),
            MainThreadExecutor()
    )

    fun diskIO(): Executor = diskIO

    fun mainThread(): Executor = mainThread

    private class MainThreadExecutor : Executor
    {
        private val mainThreadHandler = Handler( Looper.getMainLooper() )

        override fun execute( command: Runnable )
        {
            mainThreadHandler.post( command )
        }
    }
}