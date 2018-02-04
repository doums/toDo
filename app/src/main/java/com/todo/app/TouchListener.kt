package com.todo.app

import android.support.v7.widget.RecyclerView

/**
 * Created by pierre on 23/01/18.
 */
interface TouchListener {
    fun onStartSelect()
    fun onStopSelect()
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}