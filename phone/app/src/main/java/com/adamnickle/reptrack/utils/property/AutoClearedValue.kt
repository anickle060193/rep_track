package com.adamnickle.reptrack.utils.property

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T : Any>( owner: LifecycleOwner ): ReadWriteProperty<LifecycleOwner, T>
{
    private var value: T? = null

    init
    {
        owner.lifecycle.addObserver( object : LifecycleObserver {
           @OnLifecycleEvent( Lifecycle.Event.ON_DESTROY )
            fun onDestroy()
           {
               value = null
           }
        } )
    }

    override fun getValue( thisRef: LifecycleOwner, property: KProperty<*> ): T
    {
        return value ?: throw IllegalStateException( "Should never call auto-cleared-value get when it might not be available" )
    }

    override fun setValue( thisRef: LifecycleOwner, property: KProperty<*>, value: T )
    {
        this.value = value
    }
}

fun <T: Any> LifecycleOwner.autoCleared() = AutoClearedValue<T>( this )