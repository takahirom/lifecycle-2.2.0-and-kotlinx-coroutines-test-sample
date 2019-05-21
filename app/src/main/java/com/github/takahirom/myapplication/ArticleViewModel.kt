package com.github.takahirom.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay

class ArticleViewModel : ViewModel() {
    var repository = Repository()
    val articles: LiveData<List<Article>> = liveData {
        val articles = repository.articles()
        delay(1000)
        emit(articles)
    }

    val articlesByLiveData = liveData {
        val articlesLiveData = repository.articlesLiveData()
        emitSource(articlesLiveData)
    }
}

class Repository {
    suspend fun articles(): List<Article> {
        return listOf(Article("Google I/O 2019 Finished"))
    }

    fun articlesLiveData(): LiveData<List<Article>> {
        return MutableLiveData<List<Article>>().apply {
            postValue(listOf(Article("Google I/O 2019 Finished")))
        }
    }
}

data class Article(val title: String)