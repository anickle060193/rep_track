package com.adamnickle.reptrack.ui.devices

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.utils.property.LiveDataProperty
import com.garmin.android.connectiq.IQDevice

class ConnectIQDeviceItemViewModel: ViewModel()
{
    private var deviceData = MutableLiveData<IQDevice?>()

    val device by LiveDataProperty( deviceData )

    val deviceName get(): LiveData<String> = Transformations.map( deviceData ) { device -> if( device?.friendlyName.isNullOrBlank() ) "Unknown Device" else device?.friendlyName }

    val deviceId get(): LiveData<String> = Transformations.map( deviceData ) { device -> ( device?.deviceIdentifier ?: 0 ).toString() }

    val deviceStatus get(): LiveData<String> = Transformations.map( deviceData ) { device -> device?.status.toString() }

    fun bind( device: IQDevice )
    {
        this.deviceData.value = device
    }
}