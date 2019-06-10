package com.example.greg.challenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.greg.challenge.model.*
import com.example.greg.challenge.model.repository.ResultsRepository
import io.reactivex.Observable
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.net.SocketException

@RunWith(JUnit4::class)
class ResultsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<ResultStatus>

    @Mock
    private lateinit var resultsRepository : ResultsRepository

    private lateinit var resultsViewModel : ResultsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // Mock API response
        `when`(resultsRepository.getResults(ArgumentMatchers.anyString())).thenAnswer {
            Observable.just(mockRepoList())
        }

        resultsViewModel = ResultsViewModel(resultsRepository)
    }

    @Test
    fun testSearchForQueryEmitsLoadingState() {
        //setup
        resultsViewModel.results().observeForever(observer)

        //execute
        resultsViewModel.searchForQuery(ArgumentMatchers.anyString())

        //assert
        verify(observer).onChanged(StatusLoading)
    }

    @Test
    fun testSearchForBlankQueryEmitsSuccessStateWithEmptyList() {
        //setup
        resultsViewModel.results().observeForever(observer)

        //execute
        resultsViewModel.searchForQuery(ArgumentMatchers.anyString())

        //assert
        verify(observer).onChanged(StatusLoading)

        verify(observer).onChanged(StatusSuccess(listOf()))
    }

    @Test
    fun testSearchForRealQueryEmitsSuccessStateWithList() {
        //setup
        resultsViewModel.results().observeForever(observer)

        //execute
        resultsViewModel.searchForQuery(TEST_STRING)

        //assert
        verify(observer).onChanged(StatusLoading)

        verify(observer).onChanged(StatusSuccess(mockRepoList()))
        assertNotNull(resultsViewModel.results().value)
    }

    @Test
    fun testSearchEmitsErrorState() {
        // Mock response with error
        `when`(resultsRepository.getResults(ArgumentMatchers.anyString())).thenAnswer {
            Observable.error<SocketException>(SocketException(NO_INTERNET))
        }

        resultsViewModel.results().observeForever(observer)

        //execute
        resultsViewModel.searchForQuery(TEST_STRING)

        //assert
        verify(observer).onChanged(StatusLoading)

        verify(observer).onChanged(StatusError(NO_INTERNET))
    }


    private fun mockRepoList(): List<Repo> {
        val testRepo = mockRepo()
        return listOf(testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo)
    }

    private fun mockRepo() : Repo {
        return Repo(TEST_STRING, null, TEST_STRING, 0, 0, 0, TEST_STRING)
    }

    companion object{
        const val TEST_STRING = "TEST"
        const val NO_INTERNET = "NO INTERNETS"
    }
}