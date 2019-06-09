package com.example.greg.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.model.repository.ResultsRepository
import com.example.greg.challenge.view.SearchActivity.Companion.SEARCH_TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ResultsViewModel @Inject constructor(private val resultsRepository: ResultsRepository) : ViewModel() {

    private val resultsListLiveData : MutableLiveData<List<Repo>> by lazy {
        MutableLiveData<List<Repo>>()
    }

    private lateinit var resultsDisposable : Disposable

    override fun onCleared() {
        super.onCleared()
        if (::resultsDisposable.isInitialized && !resultsDisposable?.isDisposed){
            resultsDisposable?.dispose()
        }
    }

    fun searchForQuery(query: String) {

        resultsDisposable = resultsRepository.getResults(query)
            .subscribe(
                {
                    Log.d(SEARCH_TAG, "searchForQuery onNext items: ${it.size}")
                    resultsListLiveData.postValue(it)
                },
                {
                    Log.e(SEARCH_TAG, "searchForQuery error ${it.message}")
                }
            )

    }

    fun resultsList() : LiveData<List<Repo>> {
        return resultsListLiveData
    }
}