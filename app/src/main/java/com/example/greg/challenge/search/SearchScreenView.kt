package com.example.greg.challenge.search

import com.example.greg.challenge.mvi.BaseView
import io.reactivex.Observable

interface SearchScreenView : BaseView<SearchScreenViewState>{

    /**
     * Renders the view (SearchActivity) based on the SearchScreenViewState
     */
    override fun render(viewState: SearchScreenViewState)

    /**
     * The search query intent
     *
     * @return An observable emitting the search query text
     */
    fun searchQueryIntent(): Observable<CharSequence>
}
