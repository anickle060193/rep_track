package com.adamnickle.reptrack.ui.workouts

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.WindowManager
import com.adamnickle.reptrack.BuildConfig
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ActivityWorkoutsListBinding
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.utils.autoCleared
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class WorkoutsListActivity: DaggerAppCompatActivity()
{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var binding by autoCleared<ActivityWorkoutsListBinding>()

    var adapter by autoCleared<WorkoutsListAdapter>()

    private lateinit var viewModel: WorkoutsListViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView( this, R.layout.activity_workouts_list )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( WorkoutsListViewModel::class.java )

        viewModel.results.observe( this, Observer { result ->
            adapter.submitList( result?.toList() )
        } )

        this.adapter = WorkoutsListAdapter {
            workout -> println( "Workout clicked: $workout")
        }

        binding.workoutsList.adapter = adapter

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }
    }
}