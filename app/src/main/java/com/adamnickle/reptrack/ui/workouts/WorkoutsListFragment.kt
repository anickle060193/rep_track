package com.adamnickle.reptrack.ui.workouts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
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

    var binding by autoCleared<WorkoutsListFragmentBinding>()

    var adapter by autoCleared<WorkoutsListAdapter>()

    private lateinit var viewModel: WorkoutsListViewModel

    private var inputDialog: AlertDialog? = null

    companion object
    {
        fun createInstance() = WorkoutsListFragment()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        binding = DataBindingUtil.inflate( inflater, R.layout.workouts_list_fragment, container, false )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( WorkoutsListViewModel::class.java )

        viewModel.results.observe( this, Observer { result ->
            adapter.submitList( result?.sortedByDescending { workout -> workout.date } )
        } )

        adapter = WorkoutsListAdapter( appExecutors ) {
            workout -> println( "Workout clicked: $workout")
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

        ItemTouchHelper( object : ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT )
        {
            override fun onMove( recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder? ): Boolean
            {
                return false
            }

            override fun onSwiped( viewHolder: RecyclerView.ViewHolder?, direction: Int )
            {
                if( viewHolder is DataBoundViewHolder<*> )
                {
                    if( viewHolder.binding is WorkoutItemBinding )
                    {
                        viewHolder.binding.workout?.let{ workout ->
                            appExecutors.diskIO().execute {
                                workoutDao.deleteWorkout( workout )
                            }
                        }
                    }
                }
            }
        } ).attachToRecyclerView( binding.workoutsList )

        binding.workoutAdd.setOnClickListener {
            appExecutors.diskIO().execute {
                workoutDao.insertWorkout( Workout() )
            }
        }

        return binding.root
    }

    override fun onDestroy()
    {
        inputDialog?.dismiss()

        super.onDestroy()
    }
}