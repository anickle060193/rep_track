package com.adamnickle.reptrack.utils.extensions

import android.content.ContextWrapper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View

fun View.getParentActivity(): AppCompatActivity?
{
    var context = this.context
    while( context is ContextWrapper )
    {
        if( context is AppCompatActivity )
        {
            return context
        }

        context = context.baseContext
    }
    return null
}

fun RecyclerView.addDividerItemDecoration()
{
    this.addItemDecoration( DividerItemDecoration( this.context, DividerItemDecoration.VERTICAL ) )
}