package com.example.greg.challenge.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greg.challenge.model.repository.ResultsRepository
import javax.inject.Inject

class ResultsViewModel @Inject constructor(private val resultsRepository: ResultsRepository) : ViewModel() {

    fun getRepo() : ResultsRepository {
        return resultsRepository
    }


}