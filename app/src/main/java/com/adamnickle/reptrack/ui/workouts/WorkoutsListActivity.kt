package com.adamnickle.reptrack.ui.workouts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ActivityWorkoutsListBinding
import com.adamnickle.reptrack.databinding.InputDialogBinding
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.autoCleared
import dagger.android.support.DaggerAppCompatActivity
import java.util.*
import javax.inject.Inject

class WorkoutsListActivity: DaggerAppCompatActivity()
{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var workoutDao: WorkoutDao

    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<ActivityWorkoutsListBinding>()

    var adapter by autoCleared<WorkoutsListAdapter>()

    private lateinit var viewModel: WorkoutsListViewModel

    private var inputDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView( this, R.layout.activity_workouts_list )

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
        binding.workoutsList.addItemDecoration( DividerItemDecoration( this, DividerItemDecoration.VERTICAL ) )

        binding.workoutAdd.setOnClickListener {

            val inputBinding = DataBindingUtil.inflate<InputDialogBinding>( layoutInflater, R.layout.input_dialog, null, false )
            inputBinding.defaultValue = getString( R.string.shortDateFormat, Date() )

            inputDialog = AlertDialog.Builder( this )
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

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }

    override fun onDestroy()
    {
        inputDialog?.dismiss()

        super.onDestroy()
    }
}