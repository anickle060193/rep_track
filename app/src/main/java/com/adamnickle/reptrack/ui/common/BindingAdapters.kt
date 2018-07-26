package com.adamnickle.reptrack.ui.common

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.widget.TextView
import com.adamnickle.reptrack.utils.extensions.getParentActivity

@BindingAdapter( "mutableText" )
fun setMutableText( view: TextView, data: LiveData<String>? )
{
    val parentActivity = view.getParentActivity()
    if( parentActivity != null && data != null )
    {
        data.observe( parentActivity, Observer { value -> view.text = value ?: "" } )
    }
}