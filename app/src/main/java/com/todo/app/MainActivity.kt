package com.todo.app

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.ActionMode
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.os.Parcelable






class MainActivity
    :
        AppCompatActivity(),
        ClearDialogFragment.ClearDialogListener,
        ColorDialogFragment.ColorDialogListener
{
    private lateinit var adapter: TaskAdapter
    private var selectedTasks = mutableMapOf<Int, View>()
    private lateinit var actionMode: ActionMode
    private lateinit var recyclerView: RecyclerView
    var onSelect: Boolean = false

    companion object {
        private const val ADD_TASK_REQUEST = 0
        private const val SAVED_ACTION_MODE = "saved action mode"
    }

    private var actionModeCallback = object:ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater?.inflate(R.menu.menu_context, menu)
            onSelect = true
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val id = item.itemId

            return when (id) {
                R.id.action_delete_task -> {
                    for (task in selectedTasks)
                        adapter.removeTask(task.key)
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
            onSelect = false
            selectedTasks.clear()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        adapter = TaskAdapter(object : TouchListener {
            override fun onTouch(view: View, position: Int) {
                if (!onSelect) {
                    val completedCheckBox = view.findViewById(R.id.task_completed) as CheckBox
                    completedCheckBox.isChecked = !completedCheckBox.isChecked
                }
                toggleSelection(adapter.tasks[position], view)
            }

            override fun onLongTouch(view: View, position: Int) {
                if (onSelect) return
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
        val listState = recyclerView.layoutManager.onSaveInstanceState()
        savedInstanceState.putBoolean(SAVED_ACTION_MODE,  onSelect)
        savedInstanceState.putParcelable("test", listState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(SAVED_ACTION_MODE)) {
            actionMode = startActionMode(actionModeCallback)
        }
        if (savedInstanceState.getParcelable<Parcelable>("test") != null)
            Log.d("test", "onRestore")
    }

    private fun toggleSelection(task: Task, view: View) {
        if (onSelect) {
            if (selectedTasks.contains(task.id)) {
                selectedTasks.remove(task.id)
                view.setBackgroundColor(task.color)
            } else {
                selectedTasks[task.id] = view
                val draw = ResourcesCompat.getDrawable(resources, R.drawable.selected_task, null)
                draw?.setColorFilter(task.color.aRGB.toInt(), PorterDuff.Mode.OVERLAY)
                view.background = draw
            }
            if (selectedTasks.isEmpty()) actionMode.finish()
        }
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
                showClearDialog()
                true
            }
            R.id.action_add_task -> {
                val intent = Intent(this, AddTaskActivity::class.java)
                startActivityForResult(intent, ADD_TASK_REQUEST)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            val task = data?.getSerializableExtra("Task") as Task
            if (!task.description.isEmpty()) {
                var nb = -1
                adapter.tasks
                        .asSequence()
                        .filter { it.id > nb }
                        .forEach { nb = it.id }
                task.id = nb + 1
                adapter.addTask(task)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val tasks = Storage.readData(this)

        // We only want to set the tasks if the list is already empty.
        if (tasks != null && adapter.tasks.isEmpty()) {
            adapter.tasks = tasks
        }
    }

    override fun onPause() {
        super.onPause()

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
        selectedTasks.forEach({
            val taskId = it.key
            val taskView = it.value
            val draw = ResourcesCompat.getDrawable(resources, R.drawable.selected_task, null)
            adapter.tasks.firstOrNull({ it.id == taskId })?.color = color
            taskView.setBackgroundColor(color)
            draw?.setColorFilter(color.aRGB.toInt(), PorterDuff.Mode.OVERLAY)
            taskView.background = draw
        })
    }
}
