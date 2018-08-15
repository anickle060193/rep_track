package com.adamnickle.reptrack.utils

import android.content.res.Resources
import com.adamnickle.reptrack.R

object Format
{
    fun formatAcceleration( acceleration: Float, accelerationUnit: Int, accelerationFormats: Array<String> ): String
    {
        if( accelerationUnit >= accelerationFormats.size )
        {
            throw IllegalArgumentException( "Unknown acceleration unit: $accelerationUnit" )
        }

        return accelerationFormats[ accelerationUnit ].format( acceleration )
    }

    fun formatAcceleration( acceleration: Float, accelerationUnit: Int, resources: Resources ): String
    {
        val accelerationFormats = resources.getStringArray( R.array.acceleration_formats )
        return Format.formatAcceleration( acceleration, accelerationUnit, accelerationFormats )
    }
}