package com.adamnickle.reptrack.ui.uncompletedExerciseSet

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
import com.adamnickle.reptrack.utils.autoCleared
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

    private var exerciseSet: ExerciseSet? = null

    private var binding by autoCleared<UncompletedExerciseSetFragmentBinding>()

    private lateinit var viewModel: UncompletedExerciseSetFragmentViewModel

    private var listener: OnUncompletedExerciseSetFragmentInteractionListener? = null

    companion object
    {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View
    {
        val exerciseSetId = arguments?.getLong( EXERCISE_SET_ID_TAG ) ?: throw IllegalArgumentException( "Missing Exercise Set ID for Uncompleted Exercise Set Fragment" )

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.uncompleted_exercise_set_fragment,
                container,
                false
        )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( UncompletedExerciseSetFragmentViewModel::class.java )

        appExecutors.diskIO().execute {
            exerciseSet = workoutDao.getExerciseSet( exerciseSetId ) ?: throw IllegalArgumentException( "Could not find Exercise Set: $exerciseSetId" )

            exerciseSet?.also { exerciseSet ->
                appExecutors.mainThread().execute {
                    viewModel.bind( exerciseSet )
                }
            }
        }

        binding.markExerciseSetCompleted.setOnClickListener {
            exerciseSet?.let { exerciseSet ->
                appExecutors.diskIO().execute {
                    workoutDao.markExerciseSetCompleted( exerciseSet )

                    listener?.let { listener ->
                        val exercise = workoutDao.getExercise( exerciseSet.exerciseId ) ?: throw IllegalArgumentException( "Could not find Exercise: ${exerciseSet.exerciseId}" )
                        listener.onExerciseSetCompleted( exercise, exerciseSet )
                    }
                }
            }
        }

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