package com.example.greg.challenge.Search

import com.example.greg.challenge.Repo

sealed class SearchState {
    object LoadingState : SearchState()
    data class DataState(val repoList : List<Repo>) : SearchState()
    data class ErrorState(val error: String) : SearchState()
    object ClearState : SearchState()
}
