package com.todo.app

import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue

/**
 * Created by pierre on 31/01/18.
 */

// singleton
object Converter {
    var metrics: DisplayMetrics? = null

    fun convertDpToPx(dp: Float): Int {
        if (metrics == null)
            Log.e("converter", "converter not initialized ")
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
        Log.d("converter", "convertDpToPx: " + Math.round(px))

        return Math.round(px)
    }
}