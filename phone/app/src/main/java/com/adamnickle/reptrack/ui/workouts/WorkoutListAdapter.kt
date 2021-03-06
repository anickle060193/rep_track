package com.adamnickle.reptrack.ui.workouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.WorkoutItemBinding
import com.adamnickle.reptrack.model.workout.Workout
import com.adamnickle.reptrack.ui.common.DataBoundListAdapter

class WorkoutListAdapter(
        appExecutors: AppExecutors,
        private val workoutClickCallback: ( ( Workout ) -> Unit )?
): DataBoundListAdapter<Workout, WorkoutItemBinding>(
        appExecutors,
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

        binding.vm = WorkoutItemViewModel()

        binding.root.setOnClickListener {
            binding.vm?.workout?.value?.let { workout ->
                workoutClickCallback?.invoke( workout )
            }
        }

        return binding
    }

    override fun bind( binding: WorkoutItemBinding, item: Workout, position: Int )
    {
        binding.vm?.workout?.value = item
    }

}