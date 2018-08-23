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
private val Y_COLOR = Color.rgb( 0, 128, 0 )
private const val Z_COLOR = Color.BLUE
private val COMBINED_COLOR = Color.rgb( 3, 169, 244 )
private val HIGHLIGHT_COLOR = Color.argb( 150, 99, 159, 255 )

fun LineChart.setAccelerometerData( accelData: List<ExerciseSetAccel>?, allowHighlight: Boolean )
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
            xEntries.add( Entry( time, Convert.mGtoMPS( accel.x ) ).apply { data = accel } )
            yEntries.add( Entry( time, Convert.mGtoMPS( accel.y ) ).apply { data = accel } )
            zEntries.add( Entry( time, Convert.mGtoMPS( accel.z ) ).apply { data = accel } )
        }

        val dataSets = arrayOf(
            LineDataSet( xEntries, "X" ).apply {
                color = X_COLOR
            },
            LineDataSet( yEntries, "Y" ).apply {
                color = Y_COLOR
            },
            LineDataSet( zEntries, "Z" ).apply {
                color = Z_COLOR
            }
        )

        dataSets.forEach { dataSet ->
            dataSet.setDrawCircles( false )
            dataSet.isHighlightEnabled = allowHighlight
            dataSet.highLightColor = HIGHLIGHT_COLOR
            dataSet.highlightLineWidth = 3.0f
        }

        this.legend?.isEnabled = true
        this.data = LineData( *dataSets )
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

        val dataset = LineDataSet( entries, "Combined" ).apply {
            color = COMBINED_COLOR
            setDrawCircles( false )

            isHighlightEnabled = false
        }

        this.legend?.isEnabled = false
        this.data = LineData( dataset )
        this.invalidate()
    }
    else
    {
        this.clear()
    }
}