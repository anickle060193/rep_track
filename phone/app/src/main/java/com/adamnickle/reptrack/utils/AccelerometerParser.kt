package com.adamnickle.reptrack.utils

import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import kotlin.math.abs
import kotlin.math.sqrt

object AccelerometerParser
{
    private fun combineAccelXYZ( accel: ExerciseSetAccel ): Float = sqrt( accel.x * accel.x + accel.y * accel.y + accel.z * accel.z )

    private fun combineAccelXYZ( accels: List<ExerciseSetAccel> ): List<Float> = accels.map( AccelerometerParser::combineAccelXYZ )

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

    data class CombinedAccel( val time: Long, val accel: Float )

    fun getCombinedAccels( accels: List<ExerciseSetAccel> ): List<CombinedAccel> = accels.map { accel -> CombinedAccel( accel.time, combineAccelXYZ( accel ) ) }

    fun findRepsAccels( accels: List<ExerciseSetAccel>, repCount: Int ): List<List<ExerciseSetAccel>>
    {
        val combined = combineAccelXYZ( accels )

        val start = accels.minBy { accel -> accel.time }?.time ?: 0
        val end = accels.maxBy { accel -> accel.time }?.time ?: start

        val range = end - start
        val approximateRepRange = range / repCount
        val repTimeBuffer = 0.2 * approximateRepRange

        val sortedIndexes = combined.withIndex().sortedByDescending { ( _, s ) -> s }.toMutableList()

        var index = 1

        while( index < repCount
            && index < sortedIndexes.size )
        {
            val indexTime = accels[ sortedIndexes[ index ].index ].time

            var tooClose = false
            for( i in 0 until index )
            {
                val accel = accels[ sortedIndexes[ i ].index ]
                if( abs( accel.time - indexTime ) < repTimeBuffer )
                {
                    tooClose = true
                    break
                }
            }

            if( tooClose )
            {
                sortedIndexes.removeAt( index )
            }
            else
            {
                index++
            }
        }

        val highIndexes = sortedIndexes.take( repCount ).map { it.index }.sorted()

        val midpoints = mutableListOf<Int>()

        midpoints.add( 0 )

        for( i in 0 until highIndexes.size - 1 )
        {
            midpoints.add( ( highIndexes[ i ] + highIndexes[ i + 1 ] ) / 2 )
        }

        midpoints.add( accels.size - 1 )

        val reps = ArrayList<List<ExerciseSetAccel>>()

        for( i in 0 until midpoints.size - 1 )
        {
            reps.add( accels.subList( midpoints[ i ], midpoints[ i + 1 ] ) )
        }

        while( reps.size < repCount )
        {
            reps.add( emptyList() )
        }

        return reps
    }
}