package com.example.greg.challenge

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.greg.challenge.search.SearchScreenViewState
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After

@RunWith(AndroidJUnit4ClassRunner::class)
class SearchActivityTest : TestCase() {

    @get:Rule
    var activityRule: ActivityTestRule<SearchActivity>
            = ActivityTestRule(SearchActivity::class.java)

    private lateinit var activity: SearchActivity

    @Before
    public override fun setUp() {
        super.setUp()
        activity = activityRule.activity
    }

    @Test
    fun testRenderEmptyViewState() {
        activityRule.activity.render(SearchScreenViewState.EmptyDataState)

        onView(withId(R.id.no_results_view)).check(matches(isDisplayed()))
        onView(withId(R.id.results_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testRenderDataStateWithEmptyList() {
        activityRule.activity.render(SearchScreenViewState.DataState(arrayListOf()))

        onView(withId(R.id.no_results_view)).check(matches(isDisplayed()))
        onView(withId(R.id.results_recycler_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testRenderDataState() {
        activityRule.activity.render(SearchScreenViewState.DataState(mockRepoList()))

        onView(withId(R.id.no_results_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.results_recycler_view)).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.item_description), withParent(withParentIndex(0))))
            .check(matches(isDisplayed())).check(matches(withText("test")))
    }

    @Test
    fun testRenderDetailState() {
        activityRule.activity.render(SearchScreenViewState.DetailState(mockRepo()))

        onView(withId(R.id.detail_description)).check(matches(isDisplayed())).check(matches(withText("test")))
    }

    private fun mockRepoList(): ArrayList<Repo> {
        val testRepo = mockRepo()
        return arrayListOf(testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo)
    }

    private fun mockRepo() : Repo {
        return Repo("test", null, "test", 0, 0, 0, "test")
    }

    @After
    public override fun tearDown() {
        super.tearDown()
    }
}