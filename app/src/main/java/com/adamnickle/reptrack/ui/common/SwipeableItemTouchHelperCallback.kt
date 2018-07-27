package com.adamnickle.reptrack.ui.common

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View

abstract class SwipeableItemTouchHelperCallback: ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT )
{
    override fun onMove( recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean
    {
        return false
    }

    override fun onSwiped( viewHolder: RecyclerView.ViewHolder, direction: Int )
    {
        this.onDeleteItem( viewHolder )
    }
    
    protected abstract fun onDeleteItem( viewHolder: RecyclerView.ViewHolder )
    
    protected abstract fun getSwipeableView( viewHolder: RecyclerView.ViewHolder ): View

    override fun onSelectedChanged( viewHolder: RecyclerView.ViewHolder?, actionState: Int )
    {
        if( viewHolder != null )
        {
            getDefaultUIUtil().onSelected( getSwipeableView( viewHolder ) )
        }
    }

    override fun onChildDraw( c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean )
    {
        getDefaultUIUtil().onDraw( c, recyclerView, getSwipeableView( viewHolder ), dX, dY, actionState, isCurrentlyActive )
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean )
    {
        getDefaultUIUtil().onDrawOver( c, recyclerView, getSwipeableView( viewHolder ), dX, dY, actionState, isCurrentlyActive )
    }
}