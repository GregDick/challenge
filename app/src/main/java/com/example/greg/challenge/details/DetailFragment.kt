package com.example.greg.challenge.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.greg.challenge.R
import com.example.greg.challenge.Repo
import com.example.greg.challenge.results.ResultsFragment

class DetailFragment : Fragment() {

    private lateinit var repo: Repo

    private lateinit var title : TextView
    private lateinit var description : TextView
    private lateinit var size : TextView
    private lateinit var numberForks : TextView
    private lateinit var numberIssues : TextView
    private lateinit var url : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repo = arguments?.getSerializable(DETAIL_FRAGMENT_REPO) as Repo
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
//            val browserIntent = Intent()
            //todo launch browser
        }
    }


    companion object {
        private const val DETAIL_FRAGMENT_REPO = "detailFragmentRepo"
        const val DETAIL_FRAGMENT_TAG = "detailFragmentTAG"

        fun newInstance(repo : Repo): DetailFragment {
            val args = Bundle()
            args.putSerializable(DETAIL_FRAGMENT_REPO, repo)

            val fragment = DetailFragment()
            fragment.arguments = args

            return fragment
        }

    }
}