package com.adamnickle.reptrack.utils.extensions

import android.graphics.Color
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.utils.AccelerometerParser
import com.adamnickle.reptrack.utils.Convert
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

private val TIME_AXIS_FORMATTER = DecimalFormat( "0.####s", DecimalFormatSymbols.getInstance() )
private val ACCEL_AXIS_FORMATTER = DecimalFormat( "#,##0m/s\u00B2", DecimalFormatSymbols.getInstance() )

fun LineChart.initializeAccelerometerLineChart()
{
    this.description.isEnabled = false
    this.axisRight.isEnabled = false
    this.xAxis.setValueFormatter { value, _ -> TIME_AXIS_FORMATTER.format( value ) }
    this.axisLeft.setValueFormatter { value, _ -> ACCEL_AXIS_FORMATTER.format( value ) }
}

private const val X_COLOR = Color.RED
private const val Y_COLOR = -16744448
private const val Z_COLOR = Color.BLUE

fun LineChart.setAccelerometerData( accelData: List<ExerciseSetAccel>? )
{
    if( accelData != null && accelData.isNotEmpty() )
    {
        val startTime = accelData.minBy { accel -> accel.time }?.time ?: 0

        val xEntries = mutableListOf<Entry>()
        val yEntries = mutableListOf<Entry>()
        val zEntries = mutableListOf<Entry>()

        for( accel in accelData )
        {
            val time = ( accel.time.toFloat() - startTime ) / 1000.0f
            xEntries.add( Entry( time, Convert.mGtoMPS( accel.x ) ) )
            yEntries.add( Entry( time, Convert.mGtoMPS( accel.y ) ) )
            zEntries.add( Entry( time, Convert.mGtoMPS( accel.z ) ) )
        }

        val xDataSet = LineDataSet( xEntries, "X" ).apply {
            color = X_COLOR
            setDrawCircles( false )
        }
        val yDataSet = LineDataSet( yEntries, "Y" ).apply {
            color = Y_COLOR
            setDrawCircles( false )
        }
        val zDataSet = LineDataSet( zEntries, "Z" ).apply {
            color = Z_COLOR
            setDrawCircles( false )
        }

        this.data = LineData( xDataSet, yDataSet, zDataSet )
        this.invalidate()
    }
    else
    {
        this.clear()
    }
}

fun LineChart.setCombinedAccelerometerData( accelData: List<AccelerometerParser.CombinedAccel>? )
{
    if( accelData != null && accelData.isNotEmpty() )
    {
        val startTime = accelData.minBy { accel -> accel.time }?.time ?: 0

        val entries = mutableListOf<Entry>()

        for( accel in accelData )
        {
            val time = ( accel.time.toFloat() - startTime ) / 1000.0f
            entries.add( Entry( time, Convert.mGtoMPS( accel.accel ) ) )
        }

        val dataset = LineDataSet( entries, "X" ).apply {
            color = X_COLOR
            setDrawCircles( false )
        }

        this.data = LineData( dataset )
        this.invalidate()
    }
    else
    {
        this.clear()
    }
}