package com.todo.app

import android.view.View

/**
 * Created by pierre on 23/01/18.
 */
interface TouchListener {
    fun onLongTouch(view: View, position: Int)
    fun onTouch(view: View, position: Int)
}