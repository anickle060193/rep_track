package com.adamnickle.reptrack.utils

import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import org.jtransforms.fft.FloatFFT_1D
import kotlin.math.min
import kotlin.math.sqrt

object AccelerometerParser
{
    private fun combineAccelXYZ( accel: ExerciseSetAccel ): Float
    {
        return sqrt( accel.x * accel.x + accel.y * accel.y + accel.z * accel.z )
    }

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

    fun fft( accels: List<ExerciseSetAccel> ): FloatArray
    {
        val combined = combineAccelXYZ( accels )

        val bottom = combined.min() ?: 0.0f
        val top = combined.max() ?: bottom
        val mid = combined.average().toFloat()

        val range = top - bottom

        val normSamples = combined.map { accel -> ( accel - mid ) / range }

        val fft = FloatFFT_1D( normSamples.size.toLong() )

        val spectrum = normSamples.toFloatArray()

        val input = FloatArray( spectrum.size * 2 )
        for( i in spectrum.indices )
        {
            input[ i ] = spectrum[ i ]
        }

        fft.realForward( input )

        for( i in 0 until input.size )
        {
            input[ i ] *= input[ i ]
        }

        val output = FloatArray( ( input.size + 1 ) / 2 )

        for( i in output.indices )
        {
            output [ i ] = sqrt(input[ 2 * i ] * input[ 2 * i ] + input[ 2 * i + 1 ] * input[ 2 * i + 1 ] )
        }

        return output
    }
}