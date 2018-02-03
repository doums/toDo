package com.todo.app

import android.support.v7.widget.RecyclerView

/**
 * Created by pierre on 03/02/18.
 */
interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}