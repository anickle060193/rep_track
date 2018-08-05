package com.adamnickle.reptrack

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import com.adamnickle.reptrack.connectIQ.ConnectIQHelper
import com.adamnickle.reptrack.model.message.AccelerometerMessage
import com.adamnickle.reptrack.model.message.RecordingMessage
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.ui.completedExerciseSet.CompletedExerciseSetFragment
import com.adamnickle.reptrack.ui.exercise.ExerciseFragment
import com.adamnickle.reptrack.ui.shared.SharedViewModel
import com.adamnickle.reptrack.ui.workout.WorkoutFragment
import com.adamnickle.reptrack.ui.workouts.WorkoutListFragment
import com.garmin.android.connectiq.ConnectIQ
import com.garmin.android.connectiq.IQApp
import com.garmin.android.connectiq.IQDevice
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity: DaggerAppCompatActivity(),
        WorkoutListFragment.OnWorkoutsListFragmentInteractionListener,
        WorkoutFragment.OnWorkoutFragmentInteractionListener,
        ExerciseFragment.OnExerciseFragmentInteractionListener
{
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var connectIQHelper: ConnectIQHelper

    private lateinit var sharedViewModel: SharedViewModel

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
                .replace( R.id.main_content, WorkoutListFragment.newInstance() )
                .commit()

        sharedViewModel = ViewModelProviders.of( this, viewModelFactory ).get( SharedViewModel::class.java )
        sharedViewModel.deviceLiveData.observe( this, Observer( this::onDeviceChanged ) )
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

    override fun onExerciseClicked( workout: Workout, exercise: Exercise )
    {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack( "${workout.name} - ${exercise.name}" )
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .replace( R.id.main_content, ExerciseFragment.newInstance( exercise ) )
                .commit()
    }

    override fun onExerciseSetClicked( exercise: Exercise, exerciseSet: ExerciseSet )
    {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack( "${exercise.name}: Set ${exerciseSet.order}" )
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .replace( R.id.main_content, CompletedExerciseSetFragment.newInstance( exerciseSet ) )
                .commit()
    }

    private fun onDeviceChanged( device: IQDevice? )
    {
        if( device == null )
        {
            connectIQHelper.stopListeningToAll()
        }
        else
        {
            connectIQHelper.startListeningToDevice( device.deviceIdentifier, object: ConnectIQHelper.ConnectIQEventListener() {
                override fun onDeviceStatusChanged( iqDevice: IQDevice, deviceStatus: IQDevice.IQDeviceStatus )
                {
                    Toast.makeText( this@MainActivity, "Device status changed: $deviceStatus", Toast.LENGTH_LONG ).show()

                    if( deviceStatus != IQDevice.IQDeviceStatus.CONNECTED )
                    {
                        sharedViewModel.device = null
                    }
                }

                @Suppress( "UNCHECKED_CAST" )
                override fun onMessageReceived( iqDevice: IQDevice, iqApp: IQApp, messages: MutableList<Any>, messageStatus: ConnectIQ.IQMessageStatus )
                {
                    for( message in messages )
                    {
                        println( "Message: $message" )

                        if( message is Map<*, *> )
                        {
                            try
                            {
                                when( message[ "type" ] )
                                {
                                    "recording" -> handleRecordingMessage( RecordingMessage( message as Map<String, Any> ) )
                                    "accelerometer" -> handleAccelerometerDataMessage( AccelerometerMessage( message as Map<String, Any> ) )
                                    else -> println( "Unknown message: ${message[ "type" ]}" )
                                }
                            }
                            catch( e: Exception )
                            {
                                println( "Failed to handle message: $e" )
                            }
                        }
                    }
                }
            } )
        }
    }

    private fun handleRecordingMessage( message: RecordingMessage )
    {
        println( "Recording message: $message" );
    }

    private fun handleAccelerometerDataMessage( message: AccelerometerMessage )
    {
        println( "Accelerometer message: $message" )
    }
}