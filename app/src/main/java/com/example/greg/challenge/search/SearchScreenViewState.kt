package com.example.greg.challenge.search

import com.example.greg.challenge.mvi.BaseViewState
import com.example.greg.challenge.Repo

sealed class SearchScreenViewState : BaseViewState {
    object LoadingState : SearchScreenViewState()
    object EmptyDataState : SearchScreenViewState()
    object ClearState : SearchScreenViewState()
    data class DataState(val repoList : ArrayList<Repo>) : SearchScreenViewState()
    data class DetailState(val repo : Repo) : SearchScreenViewState()
    data class ErrorState(val error: String) : SearchScreenViewState()
}
