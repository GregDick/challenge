package com.example.greg.challenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greg.challenge.model.Repo
import javax.inject.Inject

class DetailViewModel @Inject constructor() : ViewModel(){

    private val detailLiveData : MutableLiveData<Repo> by lazy {
        MutableLiveData<Repo>()
    }

    fun repoSelected(repo: Repo) {
        detailLiveData.value = repo
    }

    fun detail() : LiveData<Repo> {
        return detailLiveData
    }
}