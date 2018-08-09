package com.adamnickle.reptrack.utils.property

import android.arch.lifecycle.MutableLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MutableLiveDataProperty<V>(
        private val data: MutableLiveData<V>
): ReadWriteProperty<Any, V?>
{
    override fun getValue( thisRef: Any, property: KProperty<*> ): V? = data.value

    override fun setValue( thisRef: Any, property: KProperty<*>, value: V? )
    {
        data.value = value
    }
}