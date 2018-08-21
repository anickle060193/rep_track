package com.adamnickle.reptrack.ui.workouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.WorkoutItemBinding
import com.adamnickle.reptrack.databinding.WorkoutsListFragmentBinding
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.ui.common.DataBoundViewHolder
import com.adamnickle.reptrack.ui.common.SwipeableItemTouchHelperCallback
import com.adamnickle.reptrack.ui.settings.SettingsActivity
import com.adamnickle.reptrack.utils.property.autoCleared
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WorkoutListFragment: DaggerFragment()
{
    interface OnWorkoutsListFragmentInteractionListener
    {
        fun onWorkoutClicked( workout: Workout )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var workoutDao: WorkoutDao

    @Inject
    lateinit var appExecutors: AppExecutors

    private var binding by autoCleared<WorkoutsListFragmentBinding>()

    private var adapter by autoCleared<WorkoutListAdapter>()

    private lateinit var viewModel: WorkoutListFragmentViewModel

    private var listener: OnWorkoutsListFragmentInteractionListener? = null

    companion object
    {
        fun newInstance() = WorkoutListFragment()
    }

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        setHasOptionsMenu( true )
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        binding = DataBindingUtil.inflate( inflater, R.layout.workouts_list_fragment, container, false )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( WorkoutListFragmentViewModel::class.java )

        viewModel.results.observe( this, Observer { result ->
            adapter.submitList( result?.sortedByDescending { workout -> workout.date } )
        } )

        adapter = WorkoutListAdapter( appExecutors ) { workout ->
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
                    viewHolder.binding.vm?.workout?.value?.let { workout ->
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

    override fun onCreateOptionsMenu( menu: Menu, inflater: MenuInflater )
    {
        // inflater.inflate( R.menu.workout_list_fragment, menu )
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean
    {
        return when( item.itemId )
        {
            R.id.settings -> {
                context?.let { context ->
                    startActivity( Intent( context, SettingsActivity::class.java ) )
                }
                return true
            }
            else -> super.onOptionsItemSelected( item )
        }
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
}