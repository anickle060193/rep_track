package com.adamnickle.reptrack.ui.exercise

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ExerciseSetItemBinding
import com.adamnickle.reptrack.model.workout.ExerciseSet
import com.adamnickle.reptrack.ui.common.DataBoundListAdapter

class ExerciseSetsListAdapter(
        appExecutors: AppExecutors,
        private val exerciseSetClickCallback: ( ( ExerciseSet ) -> Unit )?
): DataBoundListAdapter<ExerciseSet, ExerciseSetItemBinding>(
        appExecutors,
        object: DiffUtil.ItemCallback<ExerciseSet>()
        {
            override fun areItemsTheSame( oldItem: ExerciseSet, newItem: ExerciseSet ): Boolean
            {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame( oldItem: ExerciseSet, newItem: ExerciseSet ): Boolean
            {
                return oldItem.repCount == newItem.repCount
                && oldItem.weight == newItem.weight
                && oldItem.completed == newItem.completed
            }
        }
)
{
    override fun createBinding(parent: ViewGroup): ExerciseSetItemBinding
    {
        val binding = DataBindingUtil.inflate<ExerciseSetItemBinding>(
                LayoutInflater.from( parent.context ),
                R.layout.exercise_set_item,
                parent,
                false
        )

        binding.vm = ExerciseSetItemViewModel()

        binding.root.setOnClickListener {
            binding.vm?.exerciseSet?.let {
                exerciseSetClickCallback?.invoke( it )
            }
        }

        return binding
    }

    override fun bind( binding: ExerciseSetItemBinding, item: ExerciseSet, position: Int )
    {
        binding.vm?.bind( item, position + 1 )
    }
}