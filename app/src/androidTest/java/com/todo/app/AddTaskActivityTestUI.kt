package com.todo.app

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.CoordinatorLayout
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.isDialog
import org.junit.Assert


/**
 * Created by pierre on 05/02/18.
 */
@RunWith(AndroidJUnit4::class)
class AddTaskActivityTestUI {
    @get:Rule
    val activityRule = ActivityTestRule(AddTaskActivity::class.java, true, false)

    @Test
    fun startWithTaskToUpdate() {
        val intent = Intent()
        intent.putExtra("task", Task(0, "Test", false, MaterialColor.Blue))
        intent.putExtra("position", 0)
        activityRule.launchActivity(intent)
        onView(withId(R.id.task_description)).check(matches(withText("Test")))
        val editText = activityRule.activity.findViewById<CoordinatorLayout>(R.id.task_activity)
        val color = (editText.background as ColorDrawable).color
        Assert.assertEquals("setColor ", color, MaterialColor.Blue.aRGB.toInt())
    }

    @Test
    fun startWithoutTask() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.task_description)).check(matches(withText("")))
    }

    @Test
    fun setColorBackground() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.action_color)).perform(click())
        onView(withText("Red")).inRoot(isDialog()).perform(click())
        val editText = activityRule.activity.findViewById<CoordinatorLayout>(R.id.task_activity)
        val color = (editText.background as ColorDrawable).color
        Assert.assertEquals("setColor ", color, MaterialColor.Red.aRGB.toInt())
    }
}