package com.todo.app

import android.icu.util.Calendar
import java.io.Serializable

/**
 * Created by pierre on 20/01/18.
 */

data class Task(
        var id: Int = 0,
        var description: String = "",
        var completed: Boolean = false,
        var color: MaterialColor = MaterialColor.Grey
) : Serializable
