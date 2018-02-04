package com.todo.app

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule
import android.util.DisplayMetrics
import org.junit.*
import kotlin.math.round

@RunWith(AndroidJUnit4::class)
class ConverterTest {

    @get:Rule
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    private val metrics = DisplayMetrics()

    @Before fun setup() {
        mActivityRule.activity.windowManager.defaultDisplay.getMetrics(metrics)
        assertNotEquals("metrics init ", metrics.densityDpi, 0)
    }

    @Test
    fun test() {
        val dp = 2f
        val px: Int = round(dp * (metrics.densityDpi.toFloat() / 160f)).toInt()
        assertEquals("convertDpToPx ", px, Converter.convertDpToPx(dp))
    }

    @Test
    fun test2() {
        val dp = 10f
        val px: Int = round(dp * (metrics.densityDpi.toFloat() / 160f)).toInt()
        assertEquals("convertDpToPx ", px, Converter.convertDpToPx(dp))
    }

    @Test
    fun test3() {
        val dp = 0f
        val px: Int = round(dp * (metrics.densityDpi.toFloat() / 160f)).toInt()
        assertEquals("convertDpToPx ", px, Converter.convertDpToPx(dp))
    }
}