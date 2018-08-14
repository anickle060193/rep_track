package com.adamnickle.reptrack.ui.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
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