package com.example.greg.challenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.model.ResultStatus
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Repo>

    private val detailViewModel = DetailViewModel()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testRepoSelectedEmitsRepo() {
        //setup
        detailViewModel.detail().observeForever(observer)

        //execute
        detailViewModel.repoSelected(mockRepo())

        //assert
        verify(observer).onChanged(mockRepo())
    }

    private fun mockRepo() : Repo {
        return Repo(ResultsViewModelTest.TEST_STRING, null, ResultsViewModelTest.TEST_STRING, 0, 0, 0, ResultsViewModelTest.TEST_STRING)
    }
}