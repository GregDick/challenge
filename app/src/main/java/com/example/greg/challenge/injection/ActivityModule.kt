package com.example.greg.challenge.injection

import com.example.greg.challenge.view.SearchActivity
import com.example.greg.challenge.view.results.ResultsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    internal abstract fun contributeResultsFragment(): ResultsFragment
}