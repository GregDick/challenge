package com.example.greg.challenge.Search

import android.util.Log
import com.example.greg.challenge.MVI.BaseProcessor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchProcessor : BaseProcessor<SearchViewModel> {
    private var compositeDisposable = CompositeDisposable()
    private lateinit var viewModel : SearchViewModel

    override fun bind(viewModel: SearchViewModel) {
        this.viewModel = viewModel

        compositeDisposable.add(observeSearchQueryAction())
    }

    private fun observeSearchQueryAction(): Disposable {
        Log.d(SEARCH_PROCESSOR_TAG, "observeSearchQueryAction")
        return viewModel.searchQueryAction().subscribe()
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    companion object {
        const val SEARCH_PROCESSOR_TAG = "searchProcessorTag"
    }

}
