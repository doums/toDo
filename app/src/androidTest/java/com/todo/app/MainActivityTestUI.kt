package com.todo.app

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.support.test.espresso.intent.Intents.intending


/**
 * Created by pierre on 06/02/18.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTestUI {
    @get:Rule
    var intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun addNewTask() {
        onView(withId(R.id.action_add_task))
                .check(matches(isDisplayed()))
                .perform(click())
        intended(toPackage("com.todo.app"))

        val data = Intent()
        data.putExtra("task", Task(0, "new task", false, MaterialColor.Blue))
        data.putExtra("position", 0)
        val result = ActivityResult(Activity.RESULT_OK, data)
        intending(toPackage("com.todo.app")).respondWith(result)


    }

    @Test
    fun editNewTask() {

    }
}