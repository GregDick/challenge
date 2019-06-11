package com.example.greg.challenge.view.details

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Owner
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.SearchActivity
import junit.framework.TestCase
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DetailFragmentTest : TestCase() {

    @get:Rule
    var activityRule: ActivityTestRule<SearchActivity> = ActivityTestRule(SearchActivity::class.java)

    private lateinit var activity: SearchActivity
    private val mockRepo = mockRepo()

    @Before
    public override fun setUp() {
        super.setUp()
        activity = activityRule.activity
        activity.startDetailFragment(mockRepo)
    }

    @Test
    fun testOwnerName() {
        val expectedOwnerName = activity.getString(R.string.owner_name, mockRepo.owner?.login, mockRepo.name)

        onView(withId(R.id.detail_owner_and_name)).check(matches(withText(expectedOwnerName)))
    }

    @Test
    fun testDescription() {
        onView(withId(R.id.detail_description)).check(matches(withText(mockRepo.description)))
    }

    @Test
    fun testSize() {
        val expectedSizeString = "${mockRepo.size} KB"

        onView(withId(R.id.detail_size)).check(matches(withText(expectedSizeString)))
    }

    @Test
    fun testNumberOfForks() {
        onView(withId(R.id.detail_num_forks)).check(matches(withText(mockRepo.forks_count.toString())))
    }

    @Test
    fun testNumberOfIssues() {
        onView(withId(R.id.detail_num_issues)).check(matches(withText(mockRepo.open_issues_count.toString())))
    }

    @Test
    fun testUrlButton() {
        onView(withId(R.id.detail_url)).check(matches(withText(mockRepo.html_url)))
    }

    private fun mockRepo() : Repo {
        return Repo(TEST_NAME, Owner(TEST_LOGIN), TEST_DESC, 0, 0, 0, TEST_URL)
    }

    companion object{
        const val TEST_NAME = "Name"
        const val TEST_LOGIN = "login"
        const val TEST_DESC = "description"
        const val TEST_URL = "http://www.google.com"
    }
}


