package com.todo.app

import android.graphics.PorterDuff
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView

fun View.setBackgroundColor(color: MaterialColor) {
    this.setBackgroundColor(color.aRGB.toInt())
}

/**
 * Created by pierre on 21/01/18.
 */

class TaskAdapter(private val touchListener: TouchListener, tasks: MutableList<Task> = ArrayList())
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var tasks: MutableList<Task> = tasks
        set(value) {
            field = value
            notifyDataSetChanged()
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
        Log.d("test", "addTask")
        tasks.add(0, task)
        notifyDataSetChanged()
    }

    fun clearTasks() {
        Log.d("test", "clearTasks")
        tasks.clear()
        notifyDataSetChanged()
    }

    fun deselectTasks() {
        tasks
                .filter { it.selected }
                .forEach { it.selected = false }
    }

    fun removeSelectedTask() {
        Log.d("test", "removeSelectedTask")
        tasks.removeIf { it.selected }
    }

    fun isSelectingMode(): Boolean {
        return tasks.any { it.selected }
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
            task.position = adapterPosition
            view.setBackgroundColor(task.color)
            if (task.selected) {
                val draw = ResourcesCompat.getDrawable(view.resources, R.drawable.selected_task, null)
                draw?.setColorFilter(task.color.aRGB.toInt(), PorterDuff.Mode.OVERLAY)
                view.background = draw
            }

            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                tasks[adapterPosition].completed = isChecked
            }
        }

        override fun onClick(v: View) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                touchListener.onTouch(v, adapterPosition)
            }
        }

        override fun onLongClick(v: View): Boolean {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                touchListener.onLongTouch(v, adapterPosition)
            }
            return true
        }
    }
}