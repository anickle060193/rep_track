package com.adamnickle.reptrack.ui.shared

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.garmin.android.connectiq.IQDevice
import javax.inject.Inject

class SharedViewModel @Inject constructor(): ViewModel()
{
    val device = MutableLiveData<IQDevice?>()

    var deviceId: Long?
        get() = device.value?.deviceIdentifier
        set( value ) { value?.let { device.value = IQDevice( value, "" ) } }

    val hasDevice get() = device.value != null
}