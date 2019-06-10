package com.example.greg.challenge

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.SearchActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
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



    private fun mockRepoList(): ArrayList<Repo> {
        val testRepo = mockRepo()
        return arrayListOf(testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo, testRepo)
    }

    private fun mockRepo() : Repo {
        return Repo("test", null, "test", 0, 0, 0, "test")
    }

}