package com.todo.app


import android.app.Dialog
import android.content.Context
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v7.app.AlertDialog


/**
 * Created by pierre on 25/01/18.
 */
class ClearDialogFragment : DialogFragment() {

    private lateinit var listener: ClearDialogListener

    // Override the Fragment.onAttach() method to instantiate the ClearDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ClearDialogListener so we can send events to the host
            listener = context as ClearDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(context.toString() + " must implement ClearDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Build the dialog and set up the button click handlers
        val builder = AlertDialog.Builder(activity, R.style.dialogTheme)
        builder.setMessage(R.string.dialog_clear)
                .setPositiveButton(R.string.confirm, { _, _ ->
                    // Send the positive button event back to the host activity
                    listener.onDialogPositiveClick(this@ClearDialogFragment)
                })
                .setNegativeButton(R.string.cancel, { _, _ ->
                    // Send the negative button event back to the host activity
                    listener.onDialogNegativeClick(this@ClearDialogFragment)
                })
        return builder.create()
    }

    interface ClearDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }
}