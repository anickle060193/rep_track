package com.adamnickle.reptrack.ui.workouts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.WorkoutsListFragmentBinding
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
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

        binding.workoutAdd.setOnClickListener {

            appExecutors.diskIO().execute {
                workoutDao.insertWorkout( Workout() )
            }

            /*
            val inputBinding = DataBindingUtil.inflate<InputDialogBinding>( layoutInflater, R.layout.input_dialog, null, false )
            inputBinding.defaultValue = getString( R.string.shortDateFormat, Date() )

            context?.let { c ->
                inputDialog = AlertDialog.Builder( c )
                        .setTitle( "Workout Name" )
                        .setView( inputBinding.root )
                        .setPositiveButton( android.R.string.ok, null )
                        .setNegativeButton( android.R.string.cancel, null )
                        .create()
                        .also { dialog ->
                            dialog.window.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE )
                            dialog.setOnShowListener {
                                dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setOnClickListener {
                                    val workoutName = inputBinding.input.text.toString()
                                    if( !workoutName.isBlank() )
                                    {
                                        appExecutors.diskIO().execute {
                                            workoutDao.insertWorkout( Workout( workoutName, Date() ) )
                                        }

                                        dialog.dismiss()
                                    }
                                    else
                                    {
                                        inputBinding.input.error = "Cannot be empty"
                                    }
                                }
                            }
                        }
                inputDialog?.show()
            }
            */
        }

        return binding.root
    }

    override fun onDestroy()
    {
        inputDialog?.dismiss()

        super.onDestroy()
    }
}