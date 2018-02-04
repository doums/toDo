package com.todo.app

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker

/**
 * Created by pierre on 04/02/18.
 */
class DateDialogFragment :
        DialogFragment(),
        DatePickerDialog.OnDateSetListener
{
    private lateinit var listener: DateDialogFragment.DateDialogListener
    var year: Int = -1
    var month: Int = -1
    var day: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DateDialogFragment.DateDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement DateDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        if (year == -1) {
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        }

        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

    interface DateDialogListener {
        fun onDateSelect(year: Int, month: Int, day: Int)
    }
}