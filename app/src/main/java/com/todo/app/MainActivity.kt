package com.todo.app

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.widget.CheckBox
import java.io.Serializable
import android.view.*

class MainActivity
    :
        AppCompatActivity(),
        ClearDialogFragment.ClearDialogListener,
        ColorDialogFragment.ColorDialogListener
{
    private lateinit var adapter: TaskAdapter
    private var actionMode: ActionMode? = null
    private lateinit var recyclerView: RecyclerView
    private var selectedTask: MutableMap<Int, View> = mutableMapOf()

    companion object {
        private const val ADD_TASK_REQUEST = 0
        private const val SAVED_TASKS = "saved tasks"
    }

    private var actionModeCallback = object:ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater?.inflate(R.menu.menu_context, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val id = item.itemId

            return when (id) {
                R.id.action_delete_task -> {
                    adapter.removeSelectedTask()
                    mode.finish()
                    true
                }
                R.id.action_color -> {
                    showColorDialog()
                    true
                }
                else -> return false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            adapter.deselectTasks()
            Log.d("test", "onDestroyActionMode")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Converter.windowManager = windowManager
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        adapter = TaskAdapter(object : TouchListener {
            override fun onTouch(view: View, position: Int) {
                if (!adapter.isSelectingMode()) {
                    Log.d("test", "check")
                    val completedCheckBox = view.findViewById(R.id.task_completed) as CheckBox
                    completedCheckBox.isChecked = !completedCheckBox.isChecked
                }
                else {
                    Log.d("test", "toggleSelection")
                    toggleSelection(adapter.tasks[position], view)
                }
            }

            override fun onLongTouch(view: View, position: Int) {
                if (adapter.isSelectingMode()) return
                actionMode = startActionMode(actionModeCallback)
                toggleSelection(adapter.tasks[position], view)
            }
        })

        recyclerView = findViewById(R.id.task_list)
        recyclerView.layoutManager = getLayoutManager()
        recyclerView.adapter = adapter
        Log.d("test", "onCreate")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle ) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putSerializable(SAVED_TASKS, adapter.tasks as Serializable)
        Log.d("test", "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val tasks = savedInstanceState.getSerializable(SAVED_TASKS)
        if (tasks != null)
            adapter.tasks = tasks as MutableList<Task>
        Log.d("test", "onRestoreInstanceState")
    }

    private fun toggleSelection(task: Task, view: View) {
        task.selected = !task.selected
        view.setBackgroundColor(task)
        if (!adapter.isSelectingMode()) actionMode?.finish()
    }

    private fun getLayoutManager():  StaggeredGridLayoutManager{
        return if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            StaggeredGridLayoutManager(3, 1)
        else
            StaggeredGridLayoutManager(2, 1)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return when (id) {
            R.id.action_clear -> {
                if (!adapter.tasks.isEmpty())
                    showClearDialog()
                true
            }
            R.id.action_add_task -> {
                val intent = Intent(this, AddTaskActivity::class.java)
                Log.d("test", "start add task activity")
                startActivityForResult(intent, ADD_TASK_REQUEST)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("test", "on add task activity result")
        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            val task = data?.getSerializableExtra("Task") as Task
            if (!task.description.isEmpty()) {
                var nb = -1
                adapter.tasks
                        .asSequence()
                        .filter { it.id > nb }
                        .forEach { nb = it.id }
                task.id = nb + 1
                adapter.addTask(task, 0)
                recyclerView.scrollToPosition(0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "onResume")

        val tasks = Storage.readData(this)


        // We only want to set the tasks if the list is already empty.
        if (tasks != null && adapter.tasks.isEmpty()) {
            adapter.tasks = tasks
            Log.d("test", "resume tasks from local storage")
        }
        if (adapter.isSelectingMode()) {
            actionMode = startActionMode(actionModeCallback)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("test", "onPause")
        Storage.writeData(this, adapter.tasks)
    }

    private fun showClearDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = ClearDialogFragment()
        dialog.show(supportFragmentManager, "ClearDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Storage.clearData(this)
        adapter.clearTasks()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {}

    private fun showColorDialog() {
        val dialog = ColorDialogFragment()
        dialog.show(supportFragmentManager, "ColorDialogFragment")
    }

    override fun onColorSelect(color: MaterialColor) {
        adapter.tasks
                .filter { it.selected }
                .forEach { it.color = color }
        adapter.deselectTasks()
        actionMode?.finish()
    }
}
