package com.example.greg.challenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greg.challenge.model.*
import com.example.greg.challenge.model.repository.ResultsRepository
import com.example.greg.challenge.view.SearchActivity.Companion.SEARCH_TAG
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ResultsViewModel @Inject constructor(private val resultsRepository: ResultsRepository) : ViewModel() {

    private val resultsListLiveData : MutableLiveData<ResultStatus> by lazy {
        MutableLiveData<ResultStatus>()
    }

    private lateinit var resultsDisposable : Disposable

    override fun onCleared() {
        super.onCleared()
        if (::resultsDisposable.isInitialized && !resultsDisposable.isDisposed){
            resultsDisposable.dispose()
        }
    }

    fun searchForQuery(query: String) {
        resultsListLiveData.postValue(StatusLoading)

        if (query.isBlank()) {
            resultsListLiveData.postValue(StatusSuccess(arrayListOf())) //Github returns an error for a blank search so treat it as empty results instead
        } else {
            resultsDisposable = resultsRepository.getResults(query)
                .subscribe(
                    {
                        Log.d(SEARCH_TAG, "searchForQuery onNext items: ${it.size}")
                        resultsListLiveData.postValue(StatusSuccess(it))
                    },
                    {
                        Log.e(SEARCH_TAG, "searchForQuery error ${it.message}")
                        resultsListLiveData.postValue(StatusError(it.localizedMessage))

                    }
                )

        }
    }

    fun results() : LiveData<ResultStatus> {
        return resultsListLiveData
    }
}