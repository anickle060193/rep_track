package com.adamnickle.reptrack.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import javax.inject.Inject

class PreferenceLiveData<T>(
        private val preferenceKey: String
): LiveData<T>()
{
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String ->
        if( key == preferenceKey )
        {
            val all = sharedPreferences.all
            if( all.containsKey( preferenceKey ) )
            {
                @Suppress( "UNCHECKED_CAST" )
                this.value = all[ preferenceKey ] as T?
            }
            else
            {
                this.value = null
            }
        }
    }

    override fun onActive()
    {
        sharedPreferences.registerOnSharedPreferenceChangeListener( listener )
    }

    override fun onInactive()
    {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener( listener )
    }
}