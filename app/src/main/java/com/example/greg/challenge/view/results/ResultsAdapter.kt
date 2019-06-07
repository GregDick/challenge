package com.example.greg.challenge.view.results

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo

class ResultsAdapter(private val context: Context?, var resultsList: ArrayList<Repo>, var listener : ResultsFragment.ResultsFragmentListener) : RecyclerView.Adapter<ResultsAdapter.ResultsViewHolder>() {

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
            listener.onResultClicked(resultsList[position])
        }
    }

    class ResultsViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val nameView = view.findViewById<TextView>(R.id.item_owner_and_name)
        val description = view.findViewById<TextView>(R.id.item_description)
    }

}

