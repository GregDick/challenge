package com.example.greg.challenge.view.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.SearchActivity.Companion.SEARCH_TAG


class DetailFragment : Fragment() {

    //todo remove hardcoded test data
    private var repo = Repo("test", null, "test", 0, 0, 0, "test")

    private lateinit var title : TextView
    private lateinit var description : TextView
    private lateinit var size : TextView
    private lateinit var numberForks : TextView
    private lateinit var numberIssues : TextView
    private lateinit var url : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        title = view.findViewById(R.id.detail_owner_and_name)
        description = view.findViewById(R.id.detail_description)
        size = view.findViewById(R.id.detail_size)
        numberForks = view.findViewById(R.id.detail_num_forks)
        numberIssues = view.findViewById(R.id.detail_num_issues)
        url = view.findViewById(R.id.detail_url)

        initViews()

        return view
    }

    private fun initViews() {
        title.text = getString(R.string.owner_name, repo.owner?.login, repo.name)
        description.text = repo.description
        size.text = repo.size.toString()
        numberForks.text = repo.forks_count.toString()
        numberIssues.text = repo.open_issues_count.toString()
        url.text = repo.html_url
        url.setOnClickListener {
            Log.d(SEARCH_TAG, "starting browser")
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(repo.html_url)
            startActivity(browserIntent)
        }
    }


    companion object {
        const val DETAIL_FRAGMENT_TAG = "detailFragmentTAG"

        fun newInstance(): DetailFragment {
            return DetailFragment()
        }

    }
}