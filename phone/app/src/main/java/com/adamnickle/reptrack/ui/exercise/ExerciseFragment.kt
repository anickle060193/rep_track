package com.adamnickle.reptrack.ui.exercise

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ExerciseFragmentBinding
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.extensions.addDividerItemDecoration
import com.adamnickle.reptrack.utils.property.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ExerciseFragment: DaggerFragment()
{
    interface OnExerciseFragmentInteractionListener
    {
        fun onExerciseSetClicked( exercise: Exercise, exerciseSet: ExerciseSet )
    }

    companion object
    {
        private const val EXERCISE_ID_TAG = "exercise_id"

        fun newInstance( exercise: Exercise ): ExerciseFragment
        {
            val exerciseId = exercise.idOrThrow()

            return ExerciseFragment().apply {
                arguments = Bundle().apply {
                    putLong( EXERCISE_ID_TAG, exerciseId )
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var workoutDao: WorkoutDao

    @Inject
    lateinit var appExecutors: AppExecutors

    private var binding by autoCleared<ExerciseFragmentBinding>()

    private var adapter by autoCleared<ExerciseSetListAdapter>()

    private lateinit var viewModel: ExerciseFragmentViewModel

    private var listener: OnExerciseFragmentInteractionListener? = null

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        val exerciseId = arguments?.getLong( EXERCISE_ID_TAG ) ?: throw IllegalStateException( "No Exercise ID provided to WorkoutFragment" )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( ExerciseFragmentViewModel::class.java )

        appExecutors.diskIO().execute {
            val exercise = workoutDao.getExerciseOrThrow( exerciseId )

            appExecutors.mainThread().execute {
                viewModel.exercise.value = exercise
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        binding = DataBindingUtil.inflate( inflater, R.layout.exercise_fragment, container, false )

        viewModel.exerciseSets.observe( this, Observer { result ->
            adapter.submitList( result?.sortedBy { exerciseSet -> exerciseSet.order } )
        } )

        adapter = ExerciseSetListAdapter( appExecutors ) { exerciseSet ->
            viewModel.exercise.value?.let { exercise ->
                listener?.onExerciseSetClicked( exercise, exerciseSet )
            }
        }

        binding.exerciseSetsList.adapter = adapter
        binding.exerciseSetsList.addDividerItemDecoration()

//        ItemTouchHelper( object: SwipeableItemTouchHelperCallback( ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT )
//        {
//            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder? ): Boolean
//            {
//                if( viewHolder is DataBoundViewHolder<*>
//                    && viewHolder.binding is ExerciseSetItemBinding
//                    && target is DataBoundViewHolder<*>
//                    && target.binding is ExerciseSetItemBinding )
//                {
//                    viewHolder.binding.vm?.exerciseSet?.let { sourceExerciseSet ->
//                        target.binding.vm?.exerciseSet?.let { targetExerciseSet ->
//                            if( sourceExerciseSet.id == targetExerciseSet.id )
//                            {
//                                return false
//                            }
//
//                            appExecutors.diskIO().execute {
//                                workoutDao.swapExerciseSetsInExercise( sourceExerciseSet, targetExerciseSet )
//                            }
//                            return true
//                        }
//                    }
//                }
//
//                return false
//            }
//
//            override fun onSwiped( viewHolder: RecyclerView.ViewHolder, direction: Int )
//            {
//                if( viewHolder is DataBoundViewHolder<*>
//                    && viewHolder.binding is ExerciseSetItemBinding )
//                {
//                    viewHolder.binding.vm?.exerciseSet?.let{ exerciseSet ->
//                        appExecutors.diskIO().execute {
//                            workoutDao.deleteExerciseSet( exerciseSet )
//                        }
//                    }
//                }
//            }
//
//            override fun getSwipeableView( viewHolder: RecyclerView.ViewHolder ): View
//            {
//                if( viewHolder is DataBoundViewHolder<*>
//                    && viewHolder.binding is ExerciseSetItemBinding )
//                {
//                    return viewHolder.binding.foreground
//                }
//
//                throw IllegalArgumentException( "Bound ViewHolder is not a ${ExerciseSetItemBinding::class}" )
//            }
//
//        } ).attachToRecyclerView( binding.exerciseSetsList )

        return binding.root
    }

    override fun onAttach( context: Context)
    {
        super.onAttach( context )

        if( context is OnExerciseFragmentInteractionListener )
        {
            listener = context
        }
        else
        {
            throw ClassCastException( "$context must implement ${OnExerciseFragmentInteractionListener::class}" )
        }
    }

    override fun onDetach()
    {
        super.onDetach()

        listener = null
    }
}