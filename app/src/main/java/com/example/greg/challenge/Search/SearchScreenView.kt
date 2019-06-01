package com.example.greg.challenge.Search

import io.reactivex.Observable

interface SearchScreenView {

    /**
     * Renders the view (SearchActivity) based on the SearchScreenViewState
     */
    fun render(searchScreenViewState: SearchScreenViewState)

    /**
     * The search query intent
     *
     * @return An observable emitting the search query text
     */
    fun emitSearchQueryIntent(): Observable<String>
}
