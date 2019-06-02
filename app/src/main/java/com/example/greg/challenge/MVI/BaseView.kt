package com.example.greg.challenge.MVI

/**
 * Emits an Observable to the ViewModel in the form of an Intent
 * Only changes itself based on states received from the ViewModel
 */
interface BaseView<BaseViewState>{

    /**
     * Render the View based on the ViewState
     * This is the only way the View updates
     */
    fun render(viewState: BaseViewState)
}