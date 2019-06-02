package com.example.greg.challenge.Search

import com.example.greg.challenge.MVI.BaseView
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
