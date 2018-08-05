package com.adamnickle.reptrack.ui.shared

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.garmin.android.connectiq.IQDevice
import javax.inject.Inject

class SharedViewModel @Inject constructor(): ViewModel()
{
    private val deviceData = MutableLiveData<IQDevice?>()

    val deviceLiveData: LiveData<IQDevice?> get() = deviceData

    var device: IQDevice?
        get() = deviceData.value
        set( value ) { deviceData.value = value }

    var deviceId: Long?
        get() = deviceData.value?.deviceIdentifier
        set( value ) { value?.let { deviceData.value = IQDevice( value, "" ) } }

    val hasDevice get() = device != null
}