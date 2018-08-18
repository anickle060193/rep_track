package com.adamnickle.reptrack.ui.completedExerciseSet

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.CompletedExerciseSetFragmentBinding
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.extensions.initializeAccelerometerLineChart
import com.adamnickle.reptrack.utils.extensions.setAccelerometerData
import com.adamnickle.reptrack.utils.extensions.setCombinedAccelerometerData
import com.adamnickle.reptrack.utils.property.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CompletedExerciseSetFragment: DaggerFragment()
{
    interface OnCompletedExerciseSetFragmentInteractionListener
    {
        fun onExerciseSetUncompleted( exercise: Exercise, exerciseSet: ExerciseSet )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var workoutDao: WorkoutDao

    private var binding by autoCleared<CompletedExerciseSetFragmentBinding>()

    private var adapter by autoCleared<CompletedSetRepListAdapter>()

    private lateinit var viewModel: CompletedExerciseSetFragmentViewModel

    private var listener: OnCompletedExerciseSetFragmentInteractionListener? = null

    companion object
    {
        private const val EXERCISE_SET_ID_TAG = "exercise_set_id"

        fun newInstance( exerciseSet: ExerciseSet ): CompletedExerciseSetFragment
        {
            val exerciseSetId = exerciseSet.idOrThrow()

            return CompletedExerciseSetFragment().apply {
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

        val exerciseSetId = arguments?.getLong( EXERCISE_SET_ID_TAG ) ?: throw IllegalArgumentException( "Missing Exercise Set ID for Completed Exercise Set Fragment" )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( CompletedExerciseSetFragmentViewModel::class.java )

        appExecutors.diskIO().execute {
            val exerciseSet = workoutDao.getExerciseSetOrThrow( exerciseSetId )

            appExecutors.mainThread().execute {
                viewModel.exerciseSet.value = exerciseSet
            }
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View
    {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.completed_exercise_set_fragment,
                container,
                false
        )

        binding.vm = viewModel

        adapter = CompletedSetRepListAdapter( appExecutors ) { setRep ->
            viewModel.selectedExerciseSetRep.value = setRep
        }

        binding.repsList.adapter = adapter

        viewModel.exerciseSet.observe( this, Observer { exerciseSet ->
            adapter.submitList( ( 0 until ( exerciseSet?.repCount ?: 0 ) + 1 ).toList() )
        } )

        binding.accelerometerDataGraph.initializeAccelerometerLineChart()

        viewModel.selectedExerciseSetRepAccels.observe( this, Observer { accels ->
           binding.accelerometerDataGraph.setAccelerometerData( accels )
        } )

        binding.combinedAccelerometerDataGraph.initializeAccelerometerLineChart()

        viewModel.selectedCombinedExerciseSetRepAccels.observe( this, Observer { combined ->
            binding.combinedAccelerometerDataGraph.setCombinedAccelerometerData( combined )
        } )

        return binding.root
    }

    override fun onCreateOptionsMenu( menu: Menu, inflater: MenuInflater )
    {
        inflater.inflate( R.menu.completed_exercise_set_fragment, menu )
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean
    {
        return when( item.itemId )
        {
            R.id.unmark_exercise_set_as_completed -> {
                viewModel.exerciseSet.value?.let { exerciseSet ->
                    appExecutors.diskIO().execute {
                        workoutDao.unmarkExerciseSetCompleted( exerciseSet )

                        val exercise = workoutDao.getExerciseOrThrow( exerciseSet.exerciseId )
                        listener?.onExerciseSetUncompleted( exercise, exerciseSet )
                    }
                }
                return true
            }
            else -> super.onOptionsItemSelected( item )
        }
    }

    override fun onAttach( context: Context)
    {
        super.onAttach( context )

        if( context is CompletedExerciseSetFragment.OnCompletedExerciseSetFragmentInteractionListener)
        {
            listener = context
        }
        else
        {
            throw ClassCastException( "$context must implement ${CompletedExerciseSetFragment.OnCompletedExerciseSetFragmentInteractionListener::class}" )
        }
    }

    override fun onDetach()
    {
        super.onDetach()

        listener = null
    }
}