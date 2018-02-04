package com.todo.app

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.DisplayMetrics
import android.util.Log
import java.io.Serializable
import android.view.*
import android.support.v7.widget.helper.ItemTouchHelper

class MainActivity
    :
        AppCompatActivity(),
        ClearDialogFragment.ClearDialogListener,
        ColorDialogFragment.ColorDialogListener
{
    private lateinit var adapter: TaskAdapter
    private var actionMode: ActionMode? = null
    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val ADD_TASK_REQUEST = 0
        private const val TASKS = "saved tasks"
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
                    adapter.removeSelectedTasks()
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
            adapter.deselectAll()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        Converter.metrics = metrics
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        adapter = TaskAdapter(object : TouchListener {
            override fun onStopSelect() {
                actionMode?.finish()
            }

            override fun onStartSelect() {
                actionMode = startActionMode(actionModeCallback)
            }
        })

        recyclerView = findViewById(R.id.task_list)
        recyclerView.layoutManager = getLayoutManager()
        recyclerView.adapter = adapter

        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        Log.d("test", "onCreate")
    }

    override fun onPause() {
        super.onPause()
        Log.d("test", "onPause")
        Storage.writeData(this, adapter.tasks)
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
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle ) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putSerializable(TASKS, adapter.tasks as Serializable)
        Log.d("test", "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("test", "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        val tasks = savedInstanceState.getSerializable(TASKS)
        if (tasks != null)
            adapter.tasks = tasks as MutableList<Task>
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
            /*if (!task.description.isEmpty()) {*/    //todo uncomment for prod
                var nb = -1
                adapter.tasks
                        .asSequence()
                        .filter { it.id > nb }
                        .forEach { nb = it.id }
                task.id = nb + 1
            task.description = "id " + task.id    //todo for testing only
            task.color = MaterialColor.Blue
                adapter.addTask(task, 0)
                recyclerView.scrollToPosition(0)
            /*}*/  //todo uncomment for prod
        }
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
        adapter.recolorSelectedTasks(color)
        actionMode?.finish()
    }
}
