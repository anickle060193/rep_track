package com.adamnickle.reptrack.ui.exerciseSet

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.SetRepItemBinding
import com.adamnickle.reptrack.ui.common.DataBoundListAdapter

class SetRepsListAdapter(
        appExecutors: AppExecutors,
        private val setRepClickCallback: ( ( Int ) -> Unit )?
): DataBoundListAdapter<Int, SetRepItemBinding>(
        appExecutors,
        object: DiffUtil.ItemCallback<Int>()
        {
            override fun areItemsTheSame( oldItem: Int, newItem: Int ): Boolean
            {
                return oldItem == newItem
            }

            override fun areContentsTheSame( oldItem: Int, newItem: Int ): Boolean
            {
                return oldItem == newItem
            }
        }
)
{
    private var selectedIndex = 0

    override fun createBinding( parent: ViewGroup ): SetRepItemBinding
    {
        val binding = DataBindingUtil.inflate<SetRepItemBinding>(
                LayoutInflater.from( parent.context ),
                R.layout.set_rep_item,
                parent,
                false
        )

        binding.root.setOnClickListener {
            val oldIndex = selectedIndex
            selectedIndex = binding.repIndex


            notifyItemChanged( oldIndex )
            notifyItemChanged( selectedIndex )

            setRepClickCallback?.invoke( binding.repIndex )
        }

        return binding
    }

    override fun bind( binding: SetRepItemBinding, item: Int, position: Int )
    {
        binding.repIndex = item
        binding.selected = ( position == selectedIndex )
    }
}