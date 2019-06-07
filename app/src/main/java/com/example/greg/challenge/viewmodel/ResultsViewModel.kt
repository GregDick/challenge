package com.example.greg.challenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.model.repository.ResultsRepository
import javax.inject.Inject

class ResultsViewModel @Inject constructor(private val resultsRepository: ResultsRepository) : ViewModel() {

//    private val resultsListLiveData : MutableLiveData<List<Repo>> by lazy {
//        MutableLiveData<List<Repo>>()
//    }
    private val resultsListLiveData = MutableLiveData<List<Repo>>()

    fun searchForQuery(query: String) {
        //todo perform search and set resultsListLiveData

        val testRepo = Repo(query, null, query, 0, 0, 0, "www.google.com")
        //post value on worker thread
        resultsListLiveData.postValue(arrayListOf(testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo))
//        resultsListLiveData.value = arrayListOf(testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo) //.value from main thread
    }

    fun resultsList() : LiveData<List<Repo>> {
        return resultsListLiveData
    }
}