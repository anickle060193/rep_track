package com.adamnickle.reptrack.ui.shared

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.adamnickle.reptrack.utils.MutableLiveDataProperty
import com.garmin.android.connectiq.IQDevice
import javax.inject.Inject

class SharedViewModel @Inject constructor(): ViewModel()
{
    private val deviceLiveData = MutableLiveData<IQDevice?>()

    val deviceLive: LiveData<IQDevice?> get() = deviceLiveData

    var device by MutableLiveDataProperty( deviceLiveData )

    var deviceId: Long?
        get() = deviceLiveData.value?.deviceIdentifier
        set( value ) { value?.let { deviceLiveData.value = IQDevice( value, "" ) } }

    val hasDevice get() = device != null
}