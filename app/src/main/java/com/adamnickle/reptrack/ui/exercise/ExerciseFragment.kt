package com.adamnickle.reptrack.ui.exercise

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
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
import com.adamnickle.reptrack.utils.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ExerciseFragment: DaggerFragment()
{
    companion object
    {
        private const val EXERCISE_ID_TAG = "exercise_id"

        fun newInstance( exercise: Exercise ): ExerciseFragment
        {
            val exerciseId = exercise.id ?: throw IllegalArgumentException( "Cannot create Exercise fragment from unsaved Exercise." )

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

    private var adapter by autoCleared<ExerciseSetsListAdapter>()

    private lateinit var viewModel: ExerciseSetsListViewModel

    private var listener: OnExerciseFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        val exerciseId = arguments?.getLong( EXERCISE_ID_TAG ) ?: throw IllegalStateException( "No Exercise ID provided to WorkoutFragment" )

        binding = DataBindingUtil.inflate( inflater, R.layout.exercise_fragment, container, false )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( ExerciseSetsListViewModel::class.java )

        viewModel.exerciseSets( exerciseId ).observe( this, Observer { result ->
            adapter.submitList( result?.sortedBy { exerciseSet -> exerciseSet.order } )
        } )

        adapter = ExerciseSetsListAdapter( appExecutors ) { exerciseSet ->
            listener?.onExerciseSetClicked( exerciseSet )
        }

        binding.exerciseSetsList.adapter = adapter
        binding.exerciseSetsList.addItemDecoration( DividerItemDecoration( context, DividerItemDecoration.VERTICAL ) )

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

    interface OnExerciseFragmentInteractionListener
    {
        fun onExerciseSetClicked( exerciseSet: ExerciseSet )
    }
}