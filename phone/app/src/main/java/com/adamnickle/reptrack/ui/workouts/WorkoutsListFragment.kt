package com.adamnickle.reptrack.ui.workouts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.WorkoutItemBinding
import com.adamnickle.reptrack.databinding.WorkoutsListFragmentBinding
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.ui.common.DataBoundViewHolder
import com.adamnickle.reptrack.ui.common.SwipeableItemTouchHelperCallback
import com.adamnickle.reptrack.utils.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WorkoutsListFragment: DaggerFragment()
{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var workoutDao: WorkoutDao

    @Inject
    lateinit var appExecutors: AppExecutors

    private var binding by autoCleared<WorkoutsListFragmentBinding>()

    private var adapter by autoCleared<WorkoutsListAdapter>()

    private lateinit var viewModel: WorkoutsFragmentViewModel

    private var listener: OnWorkoutsListFragmentInteractionListener? = null

    companion object
    {
        fun newInstance() = WorkoutsListFragment()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        binding = DataBindingUtil.inflate( inflater, R.layout.workouts_list_fragment, container, false )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( WorkoutsFragmentViewModel::class.java )

        viewModel.results.observe( this, Observer { result ->
            adapter.submitList( result?.sortedByDescending { workout -> workout.date } )
        } )

        adapter = WorkoutsListAdapter( appExecutors ) { workout ->
            listener?.onWorkoutClicked( workout )
        }

        adapter.registerAdapterDataObserver( object: RecyclerView.AdapterDataObserver()
        {
            override fun onItemRangeInserted( positionStart: Int, itemCount: Int )
            {
                binding.workoutsList.smoothScrollToPosition( positionStart )
            }
        } )

        binding.workoutsList.adapter = adapter
        binding.workoutsList.addItemDecoration( DividerItemDecoration( context, DividerItemDecoration.VERTICAL ) )

        ItemTouchHelper( object: SwipeableItemTouchHelperCallback( 0, ItemTouchHelper.LEFT )
        {
            override fun onSwiped( viewHolder: RecyclerView.ViewHolder, direction: Int )
            {
                if( viewHolder is DataBoundViewHolder<*>
                 && viewHolder.binding is WorkoutItemBinding )
                {
                    viewHolder.binding.workout?.let { workout ->
                        appExecutors.diskIO().execute {
                            workoutDao.markWorkoutAsDeleted( workout )

                            Snackbar.make( binding.root, "Workout deleted", Snackbar.LENGTH_LONG )
                                    .setAction( "Undo" ) {
                                        appExecutors.diskIO().execute {
                                            workoutDao.unmarkWorkoutAsDeleted( workout )
                                        }
                                    }
                                    .show()
                        }
                    }
                }
            }

            override fun getSwipeableView(viewHolder: RecyclerView.ViewHolder): View
            {
                if( viewHolder is DataBoundViewHolder<*>
                 && viewHolder.binding is WorkoutItemBinding )
                {
                    return viewHolder.binding.foreground
                }

                throw IllegalArgumentException( "Bound ViewHolder is not a ${WorkoutItemBinding::class}" )
            }
        } ).attachToRecyclerView( binding.workoutsList )

        binding.workoutAdd.setOnClickListener {
            appExecutors.diskIO().execute {
                workoutDao.insertWorkout( Workout() )
            }
        }

        return binding.root
    }

    override fun onAttach( context: Context )
    {
        super.onAttach( context )

        if( context is OnWorkoutsListFragmentInteractionListener )
        {
            listener = context
        }
        else
        {
            throw ClassCastException( "$context must implement ${OnWorkoutsListFragmentInteractionListener::class}" )
        }
    }

    override fun onDetach()
    {
        super.onDetach()

        listener = null
    }

    interface OnWorkoutsListFragmentInteractionListener
    {
        fun onWorkoutClicked( workout: Workout )
    }
}