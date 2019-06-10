package com.example.greg.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    @Inject
    lateinit var resultsViewModelProvider : Provider<ResultsViewModel>

    @Inject
    lateinit var detailViewModelProvider : Provider<DetailViewModel>

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            return resultsViewModelProvider.get() as T
        }
        else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return detailViewModelProvider.get() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}