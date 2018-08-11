package com.adamnickle.reptrack.utils

import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import kotlin.math.min
import kotlin.math.sqrt

object AccelerometerParser
{
    private fun combineAccelXYZ( accels: List<ExerciseSetAccel> ): List<Float> = accels.map { accel ->
        sqrt( accel.x * accel.x + accel.y * accel.y + accel.z * accel.z )
    }

    data class AccelData( val max: Float, val min: Float, val avg: Float )

    fun getAccelerationData( accels: List<ExerciseSetAccel> ): AccelData
    {
        var max = Float.MIN_VALUE
        var min = Float.MAX_VALUE
        var sum = 0.0f

        for( accel in combineAccelXYZ( accels ) )
        {
            if( accel > max )
            {
                max = accel
            }

            if( accel < min )
            {
                min = accel
            }

            sum += accel
        }

        val avg = sum / accels.size

        return AccelData( max, min, avg )
    }

    fun findRepsAccels( accels: List<ExerciseSetAccel>, repCount: Int ): List<List<ExerciseSetAccel>>
    {
        val repLength = accels.size / repCount

        val reps = ArrayList<List<ExerciseSetAccel>>()

        for( i in 0 until repCount )
        {
            val start = i * repLength
            val end = min( start + repLength, accels.size )
            reps.add( accels.subList( i * repLength, end ) )
        }

        return reps
    }

    fun findReps( accels: List<ExerciseSetAccel>, repCount: Int ): List<List<Float>>
    {
        val combined = combineAccelXYZ( accels )

        val repLength = combined.size / repCount

        val reps = ArrayList<List<Float>>()

        for( i in 0 until repCount )
        {
            val start = i * repLength
            val end = min( start + repLength, combined.size )
            reps.add( combined.subList( i * repLength, end ) )
        }

        return reps
    }
}