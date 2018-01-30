package com.todo.app

import android.content.Context
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Created by pierre on 23/01/18.
 */

object Storage {
    private val LOG_TAG = Storage::class.java.simpleName
    private const val FILE_NAME = "todo_list.ser"

    fun writeData(context: Context, tasks: List<Task>?) {
        var fos: FileOutputStream? = null
        var oos: ObjectOutputStream? = null

        try {
            // Open file and write list
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            oos = ObjectOutputStream(fos)
            oos.writeObject(tasks)
            Log.d("test", "write in local storage")
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Could not write to file.")
            e.printStackTrace()
        } finally {
            try {
                oos?.close()
                fos?.close()
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Could not close the file.")
                e.printStackTrace()
            }

        }
    }

    fun readData(context: Context): MutableList<Task>? {
        var fis: FileInputStream? = null
        var ois: ObjectInputStream? = null

        var tasks: MutableList<Task>? = ArrayList()

        try {
            // Open file and read list
            fis = context.openFileInput(FILE_NAME)
            ois = ObjectInputStream(fis)

            tasks = ois.readObject() as? MutableList<Task>
            Log.d("test", "read in local storage")
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Could not read from file.")
            e.printStackTrace()
        } finally {
            try {
                ois?.close()
                fis?.close()
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Could not close the file.")
                e.printStackTrace()
            }

        }

        return tasks
    }

    fun clearData(context: Context) {
        context.deleteFile(FILE_NAME)
    }

}