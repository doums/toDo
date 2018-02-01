package com.todo.app

import android.view.WindowManager
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock

/**
 * Created by pierre on 31/01/18.
 */
internal class ConverterTest {


    @Test
    fun convertDpToPx() {
        Converter.windowManager = mock(WindowManager::class.java)

        assertEquals(5, Converter.convertDpToPx(2F), "Conversion dp to px")
    }

}