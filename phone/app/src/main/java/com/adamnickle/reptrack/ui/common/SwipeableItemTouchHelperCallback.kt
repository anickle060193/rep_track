package com.adamnickle.reptrack.ui.common

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View

abstract class SwipeableItemTouchHelperCallback( dragDirs: Int, swipeDirs: Int ): ItemTouchHelper.SimpleCallback( dragDirs, swipeDirs )
{
    override fun onMove( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder ): Boolean
    {
        return false
    }

    protected abstract fun getSwipeableView( viewHolder: RecyclerView.ViewHolder ): View

    override fun clearView( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder )
    {
        getDefaultUIUtil().clearView( getSwipeableView( viewHolder ) )
    }

    override fun onSelectedChanged( viewHolder: RecyclerView.ViewHolder?, actionState: Int )
    {
        if( actionState == ItemTouchHelper.ACTION_STATE_SWIPE )
        {
            if( viewHolder != null )
            {
                getDefaultUIUtil().onSelected( getSwipeableView( viewHolder ) )
            }
        }
        else
        {
            super.onSelectedChanged( viewHolder, actionState )
        }
    }

    override fun onChildDraw( c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean )
    {
        if( actionState == ItemTouchHelper.ACTION_STATE_SWIPE )
        {
            getDefaultUIUtil().onDraw(c, recyclerView, getSwipeableView(viewHolder), dX, dY, actionState, isCurrentlyActive)
        }
        else
        {
            super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive )
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean )
    {
        if( actionState == ItemTouchHelper.ACTION_STATE_SWIPE )
        {
            getDefaultUIUtil().onDrawOver(c, recyclerView, getSwipeableView(viewHolder), dX, dY, actionState, isCurrentlyActive)
        }
        else
        {
            super.onChildDrawOver( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive )
        }
    }
}