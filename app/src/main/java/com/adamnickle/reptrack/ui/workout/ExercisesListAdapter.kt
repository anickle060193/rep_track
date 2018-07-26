package com.adamnickle.reptrack.ui.workout

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ExerciseItemBinding
import com.adamnickle.reptrack.model.workout.Exercise
import com.adamnickle.reptrack.model.workout.WorkoutDao
import com.adamnickle.reptrack.ui.common.DataBoundListAdapter

class ExercisesListAdapter(
        appExecutors: AppExecutors,
        private val workoutDao: WorkoutDao,
        private val exerciseClickCallback: ( ( Exercise ) -> Unit )?
): DataBoundListAdapter<Exercise, ExerciseItemBinding>(
        appExecutors,
        object: DiffUtil.ItemCallback<Exercise>()
        {
            override fun areItemsTheSame( oldItem: Exercise, newItem: Exercise ): Boolean
            {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame( oldItem: Exercise, newItem: Exercise ): Boolean
            {
                return oldItem.name == newItem.name
            }
        }
)
{
    override fun createBinding(parent: ViewGroup): ExerciseItemBinding
    {
        val binding = DataBindingUtil.inflate<ExerciseItemBinding>(
                LayoutInflater.from( parent.context ),
                R.layout.exercise_item,
                parent,
                false
        )
        binding.root.setOnClickListener {
            binding.exercise?.let {
                exerciseClickCallback?.invoke( it )
            }
        }

        binding.viewModel = ExerciseViewModel( workoutDao )

        return binding
    }

    override fun bind(binding: ExerciseItemBinding, item: Exercise)
    {
        binding.exercise = item
        binding.viewModel?.bind( item )
    }
}