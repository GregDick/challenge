package com.example.greg.challenge.MVI

import com.example.greg.challenge.Search.SearchScreenView

/**
 * Receives Intents from the View and returns a ViewState to the View
 * Emits an Observable to the Processor in the form of an Action and receives a Result
 * Reduces the Result to a ViewState
 */
interface BaseViewModel<BaseView> {

    /**
     * binds the ViewModel to the View
     *      must be called after the View's views have been created?
     */
    fun bind(view : SearchScreenView)

    /**
     * unbinds the View from the ViewModel
     */
    fun unbind()

    /**
     * Reduces a Result from the Processor into a State and pushes that State back to the View
     */
    fun reduce()
}
