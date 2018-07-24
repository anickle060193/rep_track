package com.adamnickle.reptrack.ui.workouts

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.WorkoutItemBinding
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.ui.common.DataBoundListAdapter

class WorkoutsListAdapter(
        private val workoutClickCallback: ( ( Workout ) -> Unit )?
): DataBoundListAdapter<Workout, WorkoutItemBinding>(
        object: DiffUtil.ItemCallback<Workout>()
        {
            override fun areItemsTheSame( oldItem: Workout, newItem: Workout ): Boolean
            {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame( oldItem: Workout, newItem: Workout ): Boolean
            {
                return oldItem.name == newItem.name
                        && oldItem.date == newItem.date
            }
        }
)
{
    override fun createBinding(parent: ViewGroup): WorkoutItemBinding
    {
        val binding = DataBindingUtil.inflate<WorkoutItemBinding>(
                LayoutInflater.from( parent.context ),
                R.layout.workout_item,
                parent,
                false
        )
        binding.root.setOnClickListener {
            binding.workout?.let {
                workoutClickCallback?.invoke( it )
            }
        }

        return binding
    }

    override fun bind(binding: WorkoutItemBinding, item: Workout)
    {
        binding.workout = item
    }

}