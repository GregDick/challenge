package com.example.greg.challenge.mvi

/**
 * Receives Actions from the ViewModel and returns a Result
 * Emits an Observable to the ViewModel in the form of a Result
 * Handles all of the apps networking
 */
interface BaseProcessor<BaseViewModel> {

    fun bind(viewModel: BaseViewModel)

    fun unbind()
}
