package com.todo.app

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

/**
 * Created by pierre on 26/01/18.
 */

class ColorDialogFragment : DialogFragment() {

    private lateinit var listener: ColorDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ColorDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement ColorDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val colorList: MutableList<String> = mutableListOf()
        enumValues<MaterialColor>().forEach { colorList.add(it.colorName) }
        val builder = AlertDialog.Builder(activity, R.style.dialogTheme)
                .setItems(colorList.toTypedArray(), { _, which ->
                    var selectedColor = enumValues<MaterialColor>()
                            .firstOrNull({ color -> color.colorName == colorList[which] })
                    if (selectedColor == null) selectedColor = MaterialColor.Grey
                    listener.onColorSelect(selectedColor)
                })
        return builder.create()
    }

    interface ColorDialogListener {
        fun onColorSelect(color: MaterialColor)
    }
}