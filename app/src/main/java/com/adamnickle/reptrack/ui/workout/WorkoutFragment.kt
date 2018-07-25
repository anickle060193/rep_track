package com.adamnickle.reptrack.ui.workout

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
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
import com.adamnickle.reptrack.utils.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class WorkoutFragment: DaggerFragment()
{
    companion object
    {
        private const val WORKOUT_ID_TAG = "workout_id"

        fun newInstance( workout: Workout ): WorkoutFragment
        {
            val workoutId = workout.id ?: throw IllegalArgumentException( "Cannot create Workout fragment from unsaved Workout." )

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

    private var workout: Workout? = null

    private var binding by autoCleared<WorkoutFragmentBinding>()

    private var adapter by autoCleared<ExercisesListAdapter>()

    private lateinit var viewModel: WorkoutViewModel

    private var listener: OnWorkoutFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        val workoutId = arguments?.getLong( WORKOUT_ID_TAG ) ?: throw IllegalStateException( "No Workout ID provided to WorkoutFragment" )

        appExecutors.diskIO().execute {
            workout = workoutDao.getWorkout( workoutId )
        }

        binding = DataBindingUtil.inflate( inflater, R.layout.workout_fragment, container, false )

        viewModel = ViewModelProviders.of( this, viewModelFactory ).get( WorkoutViewModel::class.java )

        viewModel.exercises( workoutId ).observe( this, Observer { result ->
            adapter.submitList( result?.sortedBy { exercise -> exercise.order } )
        } )

        adapter = ExercisesListAdapter( appExecutors ) { exercise ->
            listener?.onExerciseClicked( exercise )
        }

        adapter.registerAdapterDataObserver( object: RecyclerView.AdapterDataObserver()
        {
            override fun onItemRangeInserted( positionStart: Int, itemCount: Int )
            {
                binding.exercisesList.smoothScrollToPosition( positionStart )
            }
        } )

        binding.exercisesList.adapter = adapter
        binding.exercisesList.addItemDecoration( DividerItemDecoration( context, DividerItemDecoration.VERTICAL ) )

        ItemTouchHelper( object : ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT )
        {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder? ): Boolean
            {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int )
            {
                if( viewHolder is DataBoundViewHolder<*> )
                {
                    if( viewHolder.binding is ExerciseItemBinding )
                    {
                        viewHolder.binding.exercise?.let{ exercise ->
                            appExecutors.diskIO().execute {
                                workoutDao.deleteExercise( exercise )
                            }
                        }
                    }
                }
            }
        } ).attachToRecyclerView( binding.exercisesList )

        binding.exerciseAdd.setOnClickListener {
            context?.let { context ->
                workout?.id?.also { workoutId ->
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
                            val order = workoutDao.getNextExerciseOrderForWorkoutId( workoutId )
                            val exercise = Exercise( name, workoutId, order )
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

    interface OnWorkoutFragmentInteractionListener
    {
        fun onExerciseClicked( exercise: Exercise )
    }
}