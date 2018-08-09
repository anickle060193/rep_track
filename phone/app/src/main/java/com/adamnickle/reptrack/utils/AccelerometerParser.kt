package com.adamnickle.reptrack.utils

import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import kotlin.math.sqrt

object AccelerometerParser
{
    data class AccelData( val max: Float, val min: Float, val avg: Float )

    fun getAccelerationData( accels: List<ExerciseSetAccel> ): AccelData
    {
        var max = Float.MIN_VALUE
        var min = Float.MAX_VALUE
        var sum = 0.0f

        for( accel in accels )
        {
            val a = accel.x * accel.x + accel.y * accel.y + accel.z * accel.z
            val aSqrt = sqrt( a )

            if( aSqrt > max )
            {
                max = aSqrt
            }

            if( aSqrt < min )
            {
                min = aSqrt
            }

            sum += aSqrt
        }

        val avg = sum / accels.size

        return AccelData( max, min, avg )
    }
}