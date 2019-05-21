package com.github.takahirom.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("EXPERIMENTAL_API_USAGE")
class ArticleViewModelTest1 {
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    private val testScope: TestCoroutineScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @MockK
    lateinit var repository: Repository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun testLiveData() = testScope.runBlockingTest {
        val articleViewModel = ArticleViewModel()
        articleViewModel.repository = repository
        val articlesData = listOf(Article("a"))
        coEvery {
            repository.articles()
        } returns articlesData

        val articles = articleViewModel.articles
        val observer = Observer<List<Article>> { Unit }
        try {
            articles.observeForever(observer)
            testScope.advanceTimeBy(1000)
            require(articles.value == articlesData)
        } finally {
            articles.removeObserver(observer)
        }
    }
}