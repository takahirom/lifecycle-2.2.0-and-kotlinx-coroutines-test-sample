package com.github.takahirom.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@Suppress("EXPERIMENTAL_API_USAGE")
class TestCoroutineRule : TestWatcher() {
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    val testScope: TestCoroutineScope = TestCoroutineScope(testDispatcher)

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit){
        testScope.runBlockingTest(block)
    }
}