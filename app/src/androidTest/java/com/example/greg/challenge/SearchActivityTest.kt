package com.example.greg.challenge

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.SearchActivity
import junit.framework.TestCase
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
    fun testWelcomeView() {
        onView(withId(R.id.welcome_message)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchViewDisplaysRecyclerView() {
        //execute
        onView(withId(R.id.search_src_text)).perform(typeText("TEST"))

        Thread.sleep(2500) //idling resource would be better

        //assert
        onView(withId(R.id.welcome_message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.results_recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchViewDisplaysNoResults() {
        //execute
        onView(withId(R.id.search_src_text)).perform(typeText(",.;/;[[lasjja"))

        Thread.sleep(2500) //idling resource would be better

        //assert
        onView(withId(R.id.welcome_message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.results_recycler_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.no_results_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchViewBlankQueryDisplaysNoResults() {
        //execute
        onView(withId(R.id.search_src_text)).perform(typeText(" "))

        Thread.sleep(750) //idling resource would be better

        //assert
        onView(withId(R.id.welcome_message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.results_recycler_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.no_results_view)).check(matches(isDisplayed()))
    }
}