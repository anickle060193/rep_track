package com.adamnickle.reptrack.ui.uncompletedExerciseSet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
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
import com.adamnickle.reptrack.utils.extensions.initializeAccelerometerLineChart
import com.adamnickle.reptrack.utils.extensions.setAccelerometerData
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

        binding.markExerciseSetCompleted.setOnClickListener {
            viewModel.exerciseSet.value?.let { exerciseSet ->
                appExecutors.diskIO().execute {
                    workoutDao.markExerciseSetCompleted( exerciseSet )

                    val exercise = workoutDao.getExerciseOrThrow( exerciseSet.exerciseId )
                    listener?.onExerciseSetCompleted( exercise, exerciseSet )
                }
            }
        }

        binding.accelerometerDataGraph.initializeAccelerometerLineChart()

        viewModel.accelerometerData.observe( this, Observer { accelerometerData ->
            binding.accelerometerDataGraph.setAccelerometerData( accelerometerData )
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