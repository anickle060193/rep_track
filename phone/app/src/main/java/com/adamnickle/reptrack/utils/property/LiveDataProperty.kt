package com.adamnickle.reptrack.utils.property

import android.arch.lifecycle.LiveData
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LiveDataProperty<V>(
        private val liveData: LiveData<V>
): ReadOnlyProperty<Any, V?>
{
    override fun getValue( thisRef: Any, property: KProperty<*> ): V? = liveData.value
}