package com.todo.app


import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.CheckBox
import android.widget.TextView

fun View.setBackgroundColor(color: MaterialColor) {
    this.setBackgroundColor(color.aRGB.toInt())
}

/**
 * Created by pierre on 21/01/18.
 */

class TaskAdapter(private val touchListener: TouchListener,
                  tasks: MutableList<Task> = ArrayList(),
                  var onMultiSelect: Boolean = false)
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var tasks: MutableList<Task> = tasks
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var selectedItemsIds = mutableListOf<Int>()
    private var actionMode: ActionMode? = null
    private var actionModeCallback = object:ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater?.inflate(R.menu.menu_context, menu)
            onMultiSelect = true
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            val id = item.itemId

            return when (id) {
                R.id.action_delete_task -> {
                    for (taskId in selectedItemsIds)
                        removeTask(taskId)
                    mode.finish()
                    true
                }
                else -> return false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            onMultiSelect = false
            selectedItemsIds.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder?, position: Int) {
        holder?.bindTask(tasks[position])
    }

    fun addTask(task: Task) {
        tasks.add(0, task)
        notifyDataSetChanged()
    }

    fun removeTask(id: Int) {
        tasks.remove(tasks.firstOrNull{it.id == id})
    }

    fun clearTasks() {
        tasks.clear()
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private var view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener, View.OnClickListener {

        private val descriptionTextView = view.findViewById(R.id.task_description) as TextView
        private val completedCheckBox = view.findViewById(R.id.task_completed) as CheckBox

        init {
            view.setOnLongClickListener(this)
            view.setOnClickListener(this)
        }

        fun bindTask(task: Task) {
            descriptionTextView.text = task.description
            completedCheckBox.isChecked = task.completed
            view.setBackgroundColor(task.color)

            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                tasks[adapterPosition].completed = isChecked
            }

            if (selectedItemsIds.contains(task.id)) view.setBackgroundColor(Color.LTGRAY)
            else view.setBackgroundColor(task.color)
        }

        private fun toggleSelection(task: Task) {
            if (onMultiSelect) {
                if (selectedItemsIds.contains(task.id)) {
                    selectedItemsIds.remove(task.id)
                    view.setBackgroundColor(task.color)
                } else {
                    selectedItemsIds.add(task.id)
                    view.setBackgroundColor(Color.LTGRAY)
                }
                if (selectedItemsIds.isEmpty()) actionMode?.finish()
            }
        }

        override fun onClick(v: View) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                touchListener.onTouch(v, adapterPosition)
                toggleSelection(tasks[adapterPosition])
            }
        }

        override fun onLongClick(v: View): Boolean {
            if (onMultiSelect) return true
            if (adapterPosition != RecyclerView.NO_POSITION) {
                touchListener.onLongTouch(v, adapterPosition)
                actionMode = (v.context as Activity).startActionMode(actionModeCallback)
                toggleSelection(tasks[adapterPosition])
            }
            return true
        }
    }
}