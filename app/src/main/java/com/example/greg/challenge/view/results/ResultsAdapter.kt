package com.example.greg.challenge.view.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.viewmodel.DetailViewModel

class ResultsAdapter(
    private val context: Context?,
    private var listener: ResultsFragment.ResultsFragmentListener,
    private val detailViewModel: DetailViewModel
) : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

    private val resultsList = arrayListOf<Repo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        return ResultsViewHolder(LayoutInflater.from(context).inflate(R.layout.results_repo_item, parent, false))
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.nameView.text = context?.getString(R.string.owner_name, resultsList[position].owner?.login, resultsList[position].name)
        holder.description.text = resultsList[position].description

        holder.itemView.setOnClickListener {
            detailViewModel.repoSelected(resultsList[position])
            listener.onResultClicked()
        }
    }

    fun setData(list : List<Repo>){
        if (resultsList.isNotEmpty()) {
            resultsList.clear()
        }
        resultsList.addAll(list)
        notifyDataSetChanged()
    }

    class ResultsViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.item_owner_and_name)
        val description: TextView = view.findViewById(R.id.item_description)
    }

}

