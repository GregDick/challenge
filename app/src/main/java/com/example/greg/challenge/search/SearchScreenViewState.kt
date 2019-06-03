package com.example.greg.challenge.search

import com.example.greg.challenge.mvi.BaseViewState
import com.example.greg.challenge.Repo

sealed class SearchScreenViewState : BaseViewState {
    object LoadingState : SearchScreenViewState()
    data class DataState(val repoList : ArrayList<Repo>) : SearchScreenViewState()
    object EmptyDataState : SearchScreenViewState()
    data class ErrorState(val error: String) : SearchScreenViewState()
    object ClearState : SearchScreenViewState()
}
