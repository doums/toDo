package com.todo.app

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by pierre on 04/02/18.
 */
@RunWith(AndroidJUnit4::class)
class StorageTest {
    @get:Rule
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    private val tasks = mutableListOf<Task>()

    @Test
    fun test() {
        tasks.add(Task(0, "a note", false, MaterialColor.Blue))
        tasks.add(Task(1, "an other note", true, MaterialColor.Red))
        tasks.add(Task(2, "a third note", false, MaterialColor.DeepOrange))

        Storage.writeData(mActivityRule.activity, tasks)
        val list = Storage.readData(mActivityRule.activity)

        assertEquals(tasks, list)
    }

    @Test
    fun test2() {
        Storage.clearData(mActivityRule.activity)
        val list = Storage.readData(mActivityRule.activity)
        if (list != null) assertTrue("list should be empty", list.isEmpty())
    }
}