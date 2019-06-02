package com.example.greg.challenge.Search

import com.example.greg.challenge.MVI.BaseViewState
import com.example.greg.challenge.Repo

sealed class SearchScreenViewState : BaseViewState {
    object LoadingState : SearchScreenViewState()
    data class DataState(val repoList : List<Repo>) : SearchScreenViewState()
    data class ErrorState(val error: String) : SearchScreenViewState()
    object ClearState : SearchScreenViewState()
}
