package com.adamnickle.demo.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
        private val viewModelsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create( modelClass: Class<T> ): T
    {
        val creator = viewModelsMap [ modelClass ] ?: viewModelsMap.asIterable().firstOrNull {
            modelClass.isAssignableFrom( it.key )
        }?.value ?: throw IllegalArgumentException( "Unknown model class $modelClass" )

        try
        {
            @Suppress( "UNCHECKED_CAST" )
            return creator.get() as T
        }
        catch( e: Exception )
        {
            throw RuntimeException( e )
        }
    }
}