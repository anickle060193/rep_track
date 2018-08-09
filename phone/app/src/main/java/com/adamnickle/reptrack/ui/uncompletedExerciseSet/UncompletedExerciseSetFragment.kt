package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.UncompletedExerciseSetFragmentBinding
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.property.autoCleared
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.android.support.DaggerFragment
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import javax.inject.Inject

class UncompletedExerciseSetFragment: DaggerFragment()
{
    interface OnUncompletedExerciseSetFragmentInteractionListener
    {
        fun onExerciseSetCompleted( exercise: Exercise, exerciseSet: ExerciseSet )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var workoutDao: WorkoutDao

    private var binding by autoCleared<UncompletedExerciseSetFragmentBinding>()

    private lateinit var viewModel: UncompletedExerciseSetFragmentViewModel

    private var listener: OnUncompletedExerciseSetFragmentInteractionListener? = null

    companion object
    {
        private val TIME_AXIS_FORMATTER = DecimalFormat( "0.####s", DecimalFormatSymbols.getInstance() )
        private val ACCEL_AXIS_FORMATTER = DecimalFormat( "#,##0mG", DecimalFormatSymbols.getInstance() )

        private const val EXERCISE_SET_ID_TAG = "exercise_set_id"

        fun newInstance( exerciseSet: ExerciseSet ): UncompletedExerciseSetFragment
        {
            val exerciseSetId = exerciseSet.id ?: throw IllegalArgumentException( "Cannot create Uncompleted Exercise Set fragment from unsaved Exercise Set." )

            return UncompletedExerciseSetFragment().apply {
                arguments = Bundle().apply {
                    putLong( EXERCISE_SET_ID_TAG, exerciseSetId )
                }
            }
        }
    }

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        val exerciseSetId = arguments?.getLong( EXERCISE_SET_ID_TAG ) ?: throw IllegalArgumentException( "Missing Exercise Set ID for Uncompleted Exercise Set Fragment" )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( UncompletedExerciseSetFragmentViewModel::class.java )

        appExecutors.diskIO().execute {
            val exerciseSet = workoutDao.getExerciseSet( exerciseSetId ) ?: throw IllegalArgumentException( "Could not find Exercise Set: $exerciseSetId" )

            appExecutors.mainThread().execute {
                viewModel.bind( exerciseSet )
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View
    {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.uncompleted_exercise_set_fragment,
                container,
                false
        )

        binding.vm = viewModel

        binding.rpe.setOnFocusChangeListener { _, hasFocus ->
            if( !hasFocus )
            {
                viewModel.exerciseSet?.let { exerciseSet ->
                    exerciseSet.rpe = binding.rpe.text.toString().toFloatOrNull()
                    appExecutors.diskIO().execute {
                        workoutDao.updateExerciseSet( exerciseSet )
                    }
                }
            }
        }

        binding.notes.setOnFocusChangeListener { _, hasFocus ->
            if( !hasFocus )
            {
                viewModel.exerciseSet?.let { exerciseSet ->
                    exerciseSet.notes = binding.notes.text.toString()
                    appExecutors.diskIO().execute {
                        workoutDao.updateExerciseSet( exerciseSet )
                    }
                }
            }
        }

        binding.markExerciseSetCompleted.setOnClickListener {
            viewModel.exerciseSet?.let { exerciseSet ->
                appExecutors.diskIO().execute {
                    workoutDao.markExerciseSetCompleted( exerciseSet )

                    val exercise = workoutDao.getExercise( exerciseSet.exerciseId ) ?: throw IllegalArgumentException( "Could not find Exercise: ${exerciseSet.exerciseId}" )
                    listener?.onExerciseSetCompleted( exercise, exerciseSet )
                }
            }
        }

        binding.accelerometerDataGraph.apply {
            description.isEnabled = false
            axisRight.isEnabled = false
            xAxis.setValueFormatter { value, _ -> TIME_AXIS_FORMATTER.format( value ) }
            axisLeft.setValueFormatter { value, _ -> ACCEL_AXIS_FORMATTER.format( value ) }
        }

        viewModel.accelerometerData.observe( this, Observer { accelerometerData ->
            if( accelerometerData != null && accelerometerData.isNotEmpty() )
            {
                val startTime = accelerometerData.minBy { accel -> accel.time }?.time ?: 0

                val xEntries = mutableListOf<Entry>()
                val yEntries = mutableListOf<Entry>()
                val zEntries = mutableListOf<Entry>()

                for( accel in accelerometerData )
                {
                    val time = ( accel.time.toFloat() - startTime ) / 1000.0f
                    xEntries.add( Entry( time, accel.x ) )
                    yEntries.add( Entry( time, accel.y ) )
                    zEntries.add( Entry( time, accel.z ) )
                }

                val xDataSet = LineDataSet( xEntries, "X" ).apply {
                    color = Color.RED
                    setDrawCircles( false )
                }
                val yDataSet = LineDataSet( yEntries, "Y" ).apply {
                    color = Color.GREEN
                    setDrawCircles( false )
                }
                val zDataSet = LineDataSet( zEntries, "Z" ).apply {
                    color = Color.BLUE
                    setDrawCircles( false )
                }

                binding.accelerometerDataGraph.data = LineData( xDataSet, yDataSet, zDataSet )
                binding.accelerometerDataGraph.invalidate()
            }
            else
            {
                binding.accelerometerDataGraph.clear()
            }
        } )

        return binding.root
    }

    override fun onAttach( context: Context )
    {
        super.onAttach( context )

        if( context is OnUncompletedExerciseSetFragmentInteractionListener )
        {
            listener = context
        }
        else
        {
            throw ClassCastException( "$context must implement ${OnUncompletedExerciseSetFragmentInteractionListener::class}" )
        }
    }

    override fun onDetach()
    {
        super.onDetach()

        listener = null
    }
}