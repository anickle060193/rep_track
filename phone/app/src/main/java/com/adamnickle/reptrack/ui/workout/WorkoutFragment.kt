package com.adamnickle.reptrack.ui.workout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.connectIQ.ConnectIQHelper
import com.adamnickle.reptrack.databinding.ExerciseItemBinding
import com.adamnickle.reptrack.databinding.NewExerciseDialogBinding
import com.adamnickle.reptrack.databinding.WorkoutFragmentBinding
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.ViewModelFactory
import com.adamnickle.reptrack.ui.common.DataBoundViewHolder
import com.adamnickle.reptrack.ui.common.InputDialog
import com.adamnickle.reptrack.ui.common.SwipeableItemTouchHelperCallback
import com.adamnickle.reptrack.ui.devices.SelectDeviceActivity
import com.adamnickle.reptrack.ui.shared.SharedViewModel
import com.adamnickle.reptrack.utils.extensions.addDividerItemDecoration
import com.adamnickle.reptrack.utils.property.autoCleared
import com.garmin.android.connectiq.ConnectIQ
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WorkoutFragment: DaggerFragment()
{
    interface OnWorkoutFragmentInteractionListener
    {
        fun onExerciseClicked( workout: Workout, exercise: Exercise )
        fun onWorkoutRenamed( workout: Workout )
    }

    companion object
    {
        private const val SELECT_DEVICE_REQUEST = 0

        private const val WORKOUT_ID_TAG = "workout_id"

        fun newInstance( workout: Workout ): WorkoutFragment
        {
            val workoutId = workout.idOrThrow()

            return WorkoutFragment().apply {
                arguments = Bundle().apply {
                    putLong( WORKOUT_ID_TAG, workoutId )
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

    @Inject
    lateinit var connectIQ: ConnectIQHelper

    private var binding by autoCleared<WorkoutFragmentBinding>()

    private var adapter by autoCleared<ExerciseListAdapter>()

    private lateinit var viewModel: WorkoutFragmentViewModel

    private lateinit var sharedViewModel: SharedViewModel

    private var listener: OnWorkoutFragmentInteractionListener? = null

    override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate( savedInstanceState )

        setHasOptionsMenu( true )

        activity?.also { activity ->
            sharedViewModel = ViewModelProviders.of( activity, viewModelFactory ).get( SharedViewModel::class.java )
        } ?: throw IllegalStateException( "Activity not set in Fragment.onCreate()" )

        val workoutId = arguments?.getLong( WORKOUT_ID_TAG ) ?: throw IllegalStateException( "No Workout ID provided to WorkoutFragment" )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( WorkoutFragmentViewModel::class.java )

        appExecutors.diskIO().execute {
            val workout = workoutDao.getWorkoutOrThrow( workoutId )
            appExecutors.mainThread().execute {
                viewModel.workout.value = workout
            }
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        binding = DataBindingUtil.inflate( inflater, R.layout.workout_fragment, container, false )

        viewModel.exercises.observe( this, Observer { result ->
            adapter.submitList( result?.sortedBy { exercise -> exercise.order } )
        } )

        adapter = ExerciseListAdapter( appExecutors, workoutDao ) { exercise ->
            viewModel.workout.value?.let { workout ->
                listener?.onExerciseClicked( workout, exercise )
            }
        }

        adapter.registerAdapterDataObserver( object: RecyclerView.AdapterDataObserver()
        {
            override fun onItemRangeInserted( positionStart: Int, itemCount: Int )
            {
                binding.exercisesList.smoothScrollToPosition( positionStart )
            }
        } )

        binding.exercisesList.adapter = adapter
        binding.exercisesList.addDividerItemDecoration()

        ItemTouchHelper( object: SwipeableItemTouchHelperCallback( ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT )
        {
            override fun onMove( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder ): Boolean
            {
                if( viewHolder is DataBoundViewHolder<*>
                 && viewHolder.binding is ExerciseItemBinding
                 && target is DataBoundViewHolder<*>
                 && target.binding is ExerciseItemBinding )
                {
                    viewHolder.binding.vm?.exercise?.value?.let { sourceExercise ->
                        target.binding.vm?.exercise?.value?.let { targetExercise ->
                            appExecutors.diskIO().execute {
                                workoutDao.swapExercisesInWorkout( sourceExercise, targetExercise )
                            }
                            return true
                        }
                    }
                }

                return false
            }

            override fun onSwiped( viewHolder: RecyclerView.ViewHolder, direction: Int )
            {
                if( viewHolder is DataBoundViewHolder<*>
                && viewHolder.binding is ExerciseItemBinding )
                {
                    viewHolder.binding.vm?.exercise?.value?.let{ exercise ->
                        appExecutors.diskIO().execute {
                            workoutDao.markExerciseAsDeleted( exercise )

                            Snackbar.make( binding.root, "Exercise deleted.", Snackbar.LENGTH_LONG )
                                    .setAction( "Undo" ) {
                                        appExecutors.diskIO().execute {
                                            workoutDao.unmarkExerciseAsDeleted( exercise )
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
                 && viewHolder.binding is ExerciseItemBinding )
                {
                    return viewHolder.binding.foreground
                }

                throw IllegalArgumentException( "Bound ViewHolder is not a ${ExerciseItemBinding::class}" )
            }

        } ).attachToRecyclerView( binding.exercisesList )

        binding.exerciseAdd.setOnClickListener {
            context?.let { context ->
                viewModel.workout.value?.also { workout ->
                    InputDialog.showDialog<NewExerciseDialogBinding>( context, "Exercise", R.layout.new_exercise_dialog ) { binding, dialog ->
                        val name = binding.exerciseName.text.toString()
                        if( name.isBlank() )
                        {
                            binding.exerciseName.error = "Name cannot be blank"
                            binding.exerciseName.requestFocus()
                            return@showDialog
                        }

                        val sets = binding.exerciseSets.text.toString().toIntOrNull()
                        if( sets == null || sets <= 0 )
                        {
                            binding.exerciseSets.error = "Sets must be greater than zero"
                            binding.exerciseSets.requestFocus()
                            return@showDialog
                        }

                        val reps = binding.exerciseReps.text.toString().toIntOrNull()
                        if( reps == null || reps <= 0 )
                        {
                            binding.exerciseReps.error = "Reps must be greater than zero"
                            binding.exerciseReps.requestFocus()
                            return@showDialog
                        }

                        val weight = binding.exerciseWeight.text.toString().toFloatOrNull()
                        if( weight == null || weight <= 0 )
                        {
                            binding.exerciseWeight.error = "Weight must be greater than zero"
                            binding.exerciseWeight.requestFocus()
                            return@showDialog
                        }

                        appExecutors.diskIO().execute {
                            val order = workoutDao.getNextExerciseOrderForWorkoutId( workout.idOrThrow() )
                            val exercise = Exercise( name, workout.idOrThrow(), order )
                            val exerciseId = workoutDao.insertExercise( exercise )

                            val exerciseSets = arrayListOf<ExerciseSet>()
                            for( i in 1..sets)
                            {
                                exerciseSets.add( ExerciseSet( weight, reps, exerciseId, i ) )
                            }

                            workoutDao.insertExerciseSets( exerciseSets )
                        }

                        dialog.dismiss()
                    }
                } ?: throw IllegalStateException( "Cannot create Exercise without Workout ID" )
            }
        }

        return binding.root
    }

    override fun onAttach( context: Context )
    {
        super.onAttach( context )

        if( context is OnWorkoutFragmentInteractionListener )
        {
            listener = context
        }
        else
        {
            throw ClassCastException( "$context must implement ${OnWorkoutFragmentInteractionListener::class}" )
        }
    }

    override fun onDetach()
    {
        super.onDetach()

        listener = null
    }

    override fun onCreateOptionsMenu( menu: Menu, inflater: MenuInflater )
    {
        inflater.inflate( R.menu.workout_fragment, menu )
    }

    override fun onPrepareOptionsMenu( menu: Menu )
    {
        if( !sharedViewModel.hasDevice )
        {
            menu.findItem( R.id.select_device )?.isVisible = true
            menu.findItem( R.id.open_watch_app )?.isVisible = false
            menu.findItem( R.id.send_workout_to_watch )?.isVisible = false
        }
        else
        {
            menu.findItem( R.id.select_device )?.isVisible = false
            menu.findItem( R.id.open_watch_app )?.isVisible = true
            menu.findItem( R.id.send_workout_to_watch )?.isVisible = true
        }
    }

    override fun onOptionsItemSelected( item: MenuItem ): Boolean
    {
        return when( item.itemId )
        {
            R.id.rename_workout -> {
                viewModel.workout.value?.let { workout ->
                    InputDialog.showCapWordsInputDialog( requireContext(), "Workout Name:" ) { workoutName, dialogInterface, input ->
                        if( workoutName.isBlank() )
                        {
                            input.error = "Workout name cannot be blank"
                            input.requestFocus()
                            return@showCapWordsInputDialog
                        }

                        appExecutors.diskIO().execute {
                            workout.name = workoutName
                            workoutDao.updateWorkout( workout )

                            listener?.let { listener ->
                                appExecutors.mainThread().execute {
                                    listener.onWorkoutRenamed( workout )
                                }
                            }
                        }

                        dialogInterface.dismiss()
                    }
                }
                return true
            }

            R.id.select_device -> {
                startActivityForResult( Intent( context, SelectDeviceActivity::class.java ), SELECT_DEVICE_REQUEST )
                return true
            }

            R.id.open_watch_app -> {
                sharedViewModel.deviceId?.let { deviceId ->
                    connectIQ.openApplication( deviceId, ConnectIQ.IQOpenApplicationListener { _, _, status ->
                        println( "Open App Status: $status" )
                    } )
                }
                return true
            }

            R.id.send_workout_to_watch -> {
                sendWorkoutToWatch()
                return true
            }

            else -> super.onOptionsItemSelected( item )
        }
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, data: Intent? )
    {
        if( requestCode == SELECT_DEVICE_REQUEST )
        {
            if( resultCode == Activity.RESULT_OK )
            {
                val deviceId = data?.extras?.getLong( SelectDeviceActivity.DEVICE_EXTRA )

                if( deviceId != null )
                {
                    sharedViewModel.deviceId = deviceId
                }
                else
                {
                    Snackbar.make( binding.root, "Could not find Device ID result", Snackbar.LENGTH_LONG )
                            .show()
                }
            }
        }
    }

    private fun sendWorkoutToWatch()
    {
        sharedViewModel.deviceId?.let { deviceId ->
            viewModel.workout.value?.let { workout ->
                appExecutors.diskIO().execute {
                    workoutDao.getFullWorkout( workout.idOrThrow() )?.let { workout ->
                        println( "Full Workout: $workout" )

                        val message = mapOf( "type" to "workout", "workout" to workout.toMap() )
                        connectIQ.sendMessage( deviceId, message, ConnectIQ.IQSendMessageListener { _, _, messageStatus ->
                            println( "Send Workout Status: $messageStatus" )
                        } )
                    }
                }
            }
        }
    }
}