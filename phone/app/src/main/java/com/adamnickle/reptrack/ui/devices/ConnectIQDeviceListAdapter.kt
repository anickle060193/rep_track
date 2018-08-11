package com.adamnickle.reptrack.ui.devices

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adamnickle.reptrack.AppExecutors
import com.adamnickle.reptrack.R
import com.adamnickle.reptrack.databinding.ConnectIqDeviceItemBinding
import com.adamnickle.reptrack.ui.common.DataBoundListAdapter
import com.garmin.android.connectiq.IQDevice

class ConnectIQDeviceListAdapter(
        appExecutors: AppExecutors,
        private val deviceClickCallback: ( ( IQDevice ) -> Unit )?
): DataBoundListAdapter<IQDevice, ConnectIqDeviceItemBinding>(
        appExecutors,
        object: DiffUtil.ItemCallback<IQDevice>()
        {
            override fun areItemsTheSame( oldItem: IQDevice, newItem: IQDevice ): Boolean
            {
                return oldItem.deviceIdentifier == newItem.deviceIdentifier
            }

            override fun areContentsTheSame( oldItem: IQDevice, newItem: IQDevice ): Boolean
            {
                return oldItem.deviceIdentifier == newItem.deviceIdentifier
                && oldItem.friendlyName == newItem.friendlyName
                && oldItem.status == newItem.status
            }
        }
)
{
    override fun createBinding( parent: ViewGroup ): ConnectIqDeviceItemBinding
    {
        val binding = DataBindingUtil.inflate<ConnectIqDeviceItemBinding>(
                LayoutInflater.from( parent.context ),
                R.layout.connect_iq_device_item,
                parent,
                false
        )

        binding.vm = ConnectIQDeviceItemViewModel()

        binding.root.setOnClickListener {
            binding.vm?.device?.value?.let { device ->
                deviceClickCallback?.invoke( device )
            }
        }

        return binding
    }

    override fun bind( binding: ConnectIqDeviceItemBinding, item: IQDevice, position: Int )
    {
        binding.vm?.device?.value = item
    }
}