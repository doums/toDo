package com.todo.app

import android.view.View

/**
 * Created by pierre on 03/02/18.
 */
interface ItemTouchHelperViewHolder {
    fun onItemSelected(v: View)
    fun onItemClear()
}