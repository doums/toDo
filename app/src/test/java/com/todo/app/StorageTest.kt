package com.todo.app

import android.content.Context
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`



/**
 * Created by pierre on 01/02/18.
 */
internal class StorageTest {

    private val context = mock(Context::class.java)!!
    private val tasks: List<Task> = listOf(
            Task(0, "task number 1", false, MaterialColor.BlueGrey),
            Task(2, "task number 2", true, MaterialColor.DeepOrange),
            Task(2, "task number 3", true, MaterialColor.Red)
    )

    @Test
    fun writeData() {
        Storage.writeData(context, tasks)
    }

    @Test
    fun readData() {

        val readTasks: List<Task> = Storage.readData(context) as List<Task>

        assertEquals(readTasks[0], tasks[0])
        assertEquals(readTasks[1], tasks[1])
        assertEquals(readTasks[2], tasks[2])
    }

}