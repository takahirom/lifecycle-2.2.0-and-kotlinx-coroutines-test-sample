package com.github.takahirom.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArticleViewModelTest {
    @get:Rule
    val testCoroutinesRule = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @MockK
    lateinit var repository: Repository

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun testLiveData() = testCoroutinesRule.runBlockingTest {
        val articleViewModel = ArticleViewModel()
        articleViewModel.repository = repository
        val articlesData = listOf(Article("a"))
        coEvery {
            repository.articles()
        } returns articlesData

        val articles = articleViewModel.articles
        articles.observeForTesting {
            advanceTimeBy(1000)
            require(articles.value == articlesData)
        }
    }
}