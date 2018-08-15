package com.adamnickle.reptrack.ui.common

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.adamnickle.reptrack.utils.extensions.getParentActivity

@BindingAdapter( value = [ "mutableText", "mutableFormat" ], requireAll = false )
fun <T> setMutableText( view: TextView, data: LiveData<T>?, @StringRes mutableFormat: String? )
{
    val parentActivity = view.getParentActivity()
    if( parentActivity != null && data != null )
    {
        data.observe( parentActivity, Observer { value ->
            if( mutableFormat != null )
            {

                view.text = String.format( mutableFormat, value )
            }
            else
            {
                view.text = value?.toString() ?: ""
            }
        } )
    }
}

@BindingAdapter( "visibleIf" )
fun setVisibleIf( view: View, visible: Boolean )
{
    view.visibility = if( visible ) View.VISIBLE else View.INVISIBLE
}