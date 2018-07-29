package com.adamnickle.reptrack.ui.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import com.adamnickle.reptrack.utils.extensions.getParentActivity

@BindingAdapter( "mutableText" )
fun <T> setMutableText( view: TextView, data: LiveData<T>? )
{
    val parentActivity = view.getParentActivity()
    if( parentActivity != null && data != null )
    {
        data.observe( parentActivity, Observer { value -> view.text = value?.toString() ?: "" } )
    }
}

@BindingAdapter( "visibleIf" )
fun setVisibleIf( view: View, visible: Boolean )
{
    view.visibility = if( visible ) View.VISIBLE else View.INVISIBLE
}