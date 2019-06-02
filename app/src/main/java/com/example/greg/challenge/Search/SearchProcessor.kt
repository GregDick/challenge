package com.example.greg.challenge.Search

import com.example.greg.challenge.MVI.BaseProcessor
import io.reactivex.disposables.CompositeDisposable

class SearchProcessor : BaseProcessor<SearchViewModel> {
    private var compositeDisposable = CompositeDisposable()
    private lateinit var viewModel : SearchViewModel

    override fun bind(viewModel: SearchViewModel) {
        this.viewModel = viewModel

        compositeDisposable.add(observeSearchQueryAction())
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }



}
