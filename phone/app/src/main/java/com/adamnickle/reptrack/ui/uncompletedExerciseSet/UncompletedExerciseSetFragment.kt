package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.UncompletedExerciseSetFragmentBinding
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.ExerciseSetAccel
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.extensions.initializeAccelerometerLineChart
import com.adamnickle.reptrack.utils.extensions.setAccelerometerData
import com.adamnickle.reptrack.utils.extensions.setCombinedAccelerometerData
import com.adamnickle.reptrack.utils.property.autoCleared
import dagger.android.support.DaggerFragment
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
        private const val EXERCISE_SET_ID_TAG = "exercise_set_id"

        fun newInstance( exerciseSet: ExerciseSet ): UncompletedExerciseSetFragment
        {
            val exerciseSetId = exerciseSet.idOrThrow()

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

        setHasOptionsMenu( true )

        val exerciseSetId = arguments?.getLong( EXERCISE_SET_ID_TAG ) ?: throw IllegalArgumentException( "Missing Exercise Set ID for Uncompleted Exercise Set Fragment" )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( UncompletedExerciseSetFragmentViewModel::class.java )

        appExecutors.diskIO().execute {
            val exerciseSet = workoutDao.getExerciseSetOrThrow( exerciseSetId )

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
                viewModel.exerciseSet.value?.let { exerciseSet ->
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
                viewModel.exerciseSet.value?.let { exerciseSet ->
                    exerciseSet.notes = binding.notes.text.toString()
                    appExecutors.diskIO().execute {
                        workoutDao.updateExerciseSet( exerciseSet )
                    }
                }
            }
        }

        binding.accelerometerDataGraph.initializeAccelerometerLineChart()

        viewModel.accelerometerData.observe( this, Observer { accelerometerData ->
            binding.accelerometerDataGraph.setAccelerometerData( accelerometerData, true )
        } )

        binding.combinedAccelerometerDataGraph.initializeAccelerometerLineChart()

        viewModel.combinedAccelerometerData.observe( this, Observer { combinedAccelerometerData ->
            binding.combinedAccelerometerDataGraph.setCombinedAccelerometerData( combinedAccelerometerData )
        } )

        return binding.root
    }

    override fun onCreateOptionsMenu( menu: Menu, inflater: MenuInflater )
    {
        inflater.inflate( R.menu.uncompleted_exercise_set_fragment, menu )
    }

    override fun onPrepareOptionsMenu( menu: Menu )
    {
        val hasHighlight = binding.accelerometerDataGraph.highlighted?.isNotEmpty() == true
        menu.findItem( R.id.erase_data_before_selection )?.isEnabled = hasHighlight
        menu.findItem( R.id.erase_data_after_selection )?.isEnabled = hasHighlight
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean
    {
        return when( item.itemId )
        {
            R.id.mark_exercise_set_as_completed -> {
                viewModel.exerciseSet.value?.let { exerciseSet ->
                    appExecutors.diskIO().execute {
                        workoutDao.markExerciseSetCompleted( exerciseSet )

                        val exercise = workoutDao.getExerciseOrThrow( exerciseSet.exerciseId )
                        listener?.onExerciseSetCompleted( exercise, exerciseSet )
                    }
                }
                true
            }

            R.id.erase_data_before_selection -> {
                getHighlightedAccel()?.let { exerciseSetAccel ->
                    appExecutors.diskIO().execute {
                        workoutDao.deleteExerciseSetAccelBefore( exerciseSetAccel.exerciseSetId, exerciseSetAccel.time )
                    }
                    binding.accelerometerDataGraph.highlightValue( null )
                }
                return true
            }

            R.id.erase_data_after_selection -> {
                getHighlightedAccel()?.let { exerciseSetAccel ->
                    appExecutors.diskIO().execute {
                        workoutDao.deleteExerciseSetAccelAfter( exerciseSetAccel.exerciseSetId, exerciseSetAccel.time )
                    }
                    binding.accelerometerDataGraph.highlightValue( null )
                }
                return true
            }

            else -> super.onOptionsItemSelected( item )
        }
    }

    private fun getHighlightedAccel(): ExerciseSetAccel?
    {
        binding.accelerometerDataGraph.highlighted?.firstOrNull()?.let { highlight ->
            binding.accelerometerDataGraph.data?.dataSets?.getOrNull( highlight.dataSetIndex )?.getEntryForXValue( highlight.x, highlight.y )?.let { entry ->
                entry.data?.let { data ->
                    if( data is ExerciseSetAccel )
                    {
                        return data
                    }
                }
            }
        }
        return null
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