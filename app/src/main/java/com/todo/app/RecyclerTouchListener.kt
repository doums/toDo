package com.todo.app

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.view.GestureDetector.SimpleOnGestureListener


/**
 * Created by pierre on 24/01/18.
 */

class RecyclerTouchListener(context: Context, recycleView: RecyclerView , private val touchListener: TouchListener?) : RecyclerView.OnItemTouchListener {
    private var gestureDetector: GestureDetectorCompat? = null

    init {
        gestureDetector = GestureDetectorCompat(context, object : SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
                Log.i("Gestures", "onDown !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                Log.i("Gestures", "onLongPress !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                val child = if (e != null) recycleView.findChildViewUnder(e.x, e.y) else return
                touchListener?.onLongTouch(child, recycleView.getChildAdapterPosition(child))
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        return gestureDetector?.onTouchEvent(e) ?: false
    }

    //mandatory implementations
    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}