package com.example.greg.challenge.Search

import io.reactivex.Observable

interface SearchScreenView {

    /**
     * Renders the view (SearchActivity) based on the SearchState
     */
    fun render(searchState: SearchState)

    /**
     * The search intent
     *
     * @return An observable emitting the search query text
     */
    fun searchIntent(searchQuery: String): Observable<String>
}
