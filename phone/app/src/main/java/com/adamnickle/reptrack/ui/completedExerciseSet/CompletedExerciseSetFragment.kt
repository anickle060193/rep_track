package com.adamnickle.reptrack.ui.completedExerciseSet

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ExerciseSetFragmentBinding
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CompletedExerciseSetFragment: DaggerFragment()
{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var workoutDao: WorkoutDao

    var exerciseSet: ExerciseSet? = null

    private var binding by autoCleared<ExerciseSetFragmentBinding>()

    private var adapter by autoCleared<CompletedSetRepListAdapter>()

    lateinit var viewModel: CompletedExerciseSetFragmentViewModel

    companion object
    {
        private const val EXERCISE_SET_ID_TAG = "exercise_set_id"

        fun newInstance( exerciseSet: ExerciseSet ): CompletedExerciseSetFragment
        {
            val exerciseSetId = exerciseSet.id ?: throw IllegalArgumentException( "Cannot create Exercise Set fragment from unsaved Exercise Set." )

            return CompletedExerciseSetFragment().apply {
                arguments = Bundle().apply {
                    putLong( EXERCISE_SET_ID_TAG, exerciseSetId )
                }
            }
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View
    {
        val exerciseSetId = arguments?.getLong( EXERCISE_SET_ID_TAG ) ?: throw IllegalArgumentException( "Missing Exercise Set ID for Exercise Set Fragment" )

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.exercise_set_fragment,
                container,
                false
        )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( CompletedExerciseSetFragmentViewModel::class.java )

        adapter = CompletedSetRepListAdapter( appExecutors ) { setRep ->
            println( "Set Rep Clicked: $setRep" )
        }

        binding.repsList.adapter = adapter

        appExecutors.diskIO().execute {
            exerciseSet = workoutDao.getExerciseSet( exerciseSetId ) ?: throw IllegalArgumentException( "Could not find Exercise Set: $exerciseSetId" )

            exerciseSet?.also { exerciseSet ->
                appExecutors.mainThread().execute {
                    viewModel.bind( exerciseSet )
                }

                adapter.submitList( ( 0 until exerciseSet.repCount ).toList() )
            }
        }

        return binding.root
    }
}