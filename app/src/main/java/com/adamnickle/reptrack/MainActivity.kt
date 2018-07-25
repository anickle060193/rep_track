package com.adamnickle.reptrack

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import android.view.WindowManager
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.ui.workout.WorkoutFragment
import com.adamnickle.reptrack.ui.workouts.WorkoutsListFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: DaggerAppCompatActivity(), WorkoutsListFragment.OnWorkoutsListFragmentInteractionListener, WorkoutFragment.OnWorkoutFragmentInteractionListener
{
    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.main_activity )
        setSupportActionBar( toolbar )

        if( BuildConfig.DEBUG )
        {
            window.addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON )
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val backStackCount = supportFragmentManager.backStackEntryCount
            val index = backStackCount - 1
            if( index >= 0 )
            {
                val entry = supportFragmentManager.getBackStackEntryAt( index )
                supportActionBar?.title = entry.name
            }
            else
            {
                supportActionBar?.setTitle( R.string.app_name )
            }

            supportActionBar?.setDisplayHomeAsUpEnabled( backStackCount > 0 )
        }

        supportFragmentManager
                .beginTransaction()
                .replace( R.id.main_content, WorkoutsListFragment.newInstance() )
                .commit()
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean
    {
        return when( item.itemId )
        {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected( item )
        }
    }

    override fun onWorkoutClicked( workout: Workout )
    {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack( "Workout - ${workout.name}" )
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .replace( R.id.main_content, WorkoutFragment.newInstance( workout ) )
                .commit()
    }

    override fun onExerciseClicked( exercise: Exercise )
    {
        println( "Exercise clicked: $exercise" )
    }
}