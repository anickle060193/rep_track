package com.adamnickle.reptrack.ui.common

import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup

abstract class DataBoundListAdapter<T, V: ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
): ListAdapter<T, DataBoundViewHolder<V>>( diffCallback )
{
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): DataBoundViewHolder<V>
    {
        val binding = createBinding( parent )
        return DataBoundViewHolder( binding )
    }

    protected abstract fun createBinding( parent: ViewGroup ): V

    override fun onBindViewHolder( holder: DataBoundViewHolder<V>, position: Int )
    {
        bind( holder.binding, getItem( position ) )
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind( binding: V, item: T )
}