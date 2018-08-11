package com.adamnickle.reptrack.ui.devices

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.garmin.android.connectiq.IQDevice

class ConnectIQDeviceItemViewModel: ViewModel()
{
    val device = MutableLiveData<IQDevice?>()

    val deviceName get(): LiveData<String> = Transformations.map( device ) { device ->
        if( device?.friendlyName.isNullOrBlank() ) "Unknown Device" else device?.friendlyName
    }

    val deviceId get(): LiveData<String> = Transformations.map( device ) { device -> ( device?.deviceIdentifier ?: 0 ).toString() }

    val deviceStatus get(): LiveData<String> = Transformations.map( device ) { device -> device?.status.toString() }
}