package com.adamnickle.reptrack.ui.common

import android.content.SharedPreferences
import android.content.res.Resources
import com.adamnickle.reptrack.R

object UiUtil
{
    fun getAccelerationFormat( resources: Resources, sharedPreferences: SharedPreferences ): Int
    {
        val accelerationFormats = resources.getIntArray( R.array.acceleration_formats )
        val accelerationUnit = sharedPreferences.getInt( "acceleration_units", 0 )

        if( accelerationUnit >= accelerationFormats.size )
        {
            throw IllegalArgumentException( "Unknown acceleration unit: $accelerationUnit" )
        }

        return accelerationFormats[ accelerationUnit ]
    }
}