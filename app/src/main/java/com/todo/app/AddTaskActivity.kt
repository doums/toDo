package com.todo.app

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import java.io.Serializable

class AddTaskActivity : AppCompatActivity(), ColorDialogFragment.ColorDialogListener {

    private var task: Task = Task()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Get a support ActionBar corresponding to this toolbar
        val ab = supportActionBar
        // Enable the Up button
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return when (id) {
            R.id.action_confirm_task -> {
                val data = Intent()
                val description = findViewById(R.id.task_description) as? EditText
                task.description = description?.text.toString()
                data.putExtra("Task", task as Serializable)
                setResult(Activity.RESULT_OK, data)
                finish()
                true
            }
            R.id.action_color -> {
                showColorDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showColorDialog() {
        val dialog = ColorDialogFragment()
        dialog.show(supportFragmentManager, "ColorDialogFragment")
    }

    override fun onColorSelect(color: MaterialColor) {
        val view = findViewById(R.id.task_activity) as? View
        view?.setBackgroundColor(color)
        task.color = color
    }
}
