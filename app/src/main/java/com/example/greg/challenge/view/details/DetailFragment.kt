package com.example.greg.challenge.view.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.SearchActivity.Companion.SEARCH_TAG
import com.example.greg.challenge.viewmodel.ViewModelFactory
import com.google.android.material.button.MaterialButton
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class DetailFragment : Fragment() {

    private lateinit var title : TextView
    private lateinit var description : TextView
    private lateinit var size : TextView
    private lateinit var numberForks : TextView
    private lateinit var numberIssues : TextView
    private lateinit var url : MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        title = view.findViewById(R.id.detail_owner_and_name)
        description = view.findViewById(R.id.detail_description)
        size = view.findViewById(R.id.detail_size)
        numberForks = view.findViewById(R.id.detail_num_forks)
        numberIssues = view.findViewById(R.id.detail_num_issues)
        url = view.findViewById(R.id.detail_url)

        bindDataToView()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {

            val appCompatActivity = activity as AppCompatActivity
            appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            appCompatActivity.supportActionBar?.setDisplayShowHomeEnabled(true)
        }

    }

    private fun bindDataToView() {
        val repo = arguments?.getParcelable<Repo>(DETAIL_FRAGMENT_REPO)

        repo?.let {
            title.text = getString(R.string.owner_name, it.owner?.login, it.name)
            description.text = it.description
            size.text = formatRepoSize(it.size)
            numberForks.text = it.forks_count.toString()
            numberIssues.text = it.open_issues_count.toString()
            url.text = it.html_url
            url.setOnClickListener {
                Log.d(SEARCH_TAG, "starting browser")
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse(repo.html_url)
                startActivity(browserIntent)
            }
        }

    }

    private fun formatRepoSize(repoSize: Int?): String {
        if (repoSize == null) {
            return "null"
        }
        //convert to MB
        if (repoSize in 1000..998999) {
            return String.format("%.2f MB", (repoSize.toDouble() / 1000))
        }

        //convert to GB
        else if (repoSize > 998999){
            return String.format("%.2f GB", (repoSize.toDouble() / 1000000))
        }

        return "$repoSize KB"
    }


    companion object {
        const val DETAIL_FRAGMENT_TAG = "detailFragmentTAG"
        const val DETAIL_FRAGMENT_REPO = "extra:detailRepo"

        fun newInstance(repo: Repo): DetailFragment {
            val args = Bundle()
            val detailFragment = DetailFragment()

            args.putParcelable(DETAIL_FRAGMENT_REPO, repo)
            detailFragment.arguments = args

            return detailFragment
        }

    }
}