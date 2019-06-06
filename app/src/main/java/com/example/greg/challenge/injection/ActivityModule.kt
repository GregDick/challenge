package com.example.greg.challenge.injection

import com.example.greg.challenge.view.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSearchActivity(): SearchActivity

}