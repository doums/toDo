package com.todo.app

import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager

/**
 * Created by pierre on 31/01/18.
 */

// singleton
object Converter {
    var windowManager: WindowManager? = null

    fun convertDpToPx(dp: Float): Int {
        if (windowManager == null)
            Log.e("test", "converter not initialized ")
        val metrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(metrics)
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
        Log.d("test", "convertDpToPx: " + Math.round(px))

        return Math.round(px)
    }
}