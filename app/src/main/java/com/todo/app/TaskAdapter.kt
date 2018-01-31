package com.todo.app

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView

fun View.setCardStyle(color: Int = MaterialColor.Grey.aRGB.toInt(), pickedUp: Boolean = false) {
    (this as CardView).setCardBackgroundColor(color)
    if (pickedUp) {
        this.cardElevation = Converter.convertDpToPx(8F).toFloat()
    }
    else  {
        this.cardElevation = Converter.convertDpToPx(2F).toFloat()
    }
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

    fun addTask(task: Task, position: Int) {
        Log.d("test", "addTask")
        tasks.add(position, task)
        notifyItemInserted(position)
    }

    fun clearTasks() {
        Log.d("test", "clearTasks")
        tasks.clear()
        notifyDataSetChanged()
    }

    fun removeTask(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class TaskViewHolder(private var view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener, View.OnClickListener {

        private val descriptionTextView = view.findViewById(R.id.task_description) as TextView
        private val completedCheckBox = view.findViewById(R.id.task_completed) as CheckBox

        init {
            view.setOnLongClickListener(this)
            view.setOnClickListener(this)
        }

        fun bindTask(task: Task) {
            Log.d("viewHolder", "bindTask")
            descriptionTextView.text = task.description
            completedCheckBox.isChecked = task.completed
            view.setCardStyle(task.color.aRGB.toInt())
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