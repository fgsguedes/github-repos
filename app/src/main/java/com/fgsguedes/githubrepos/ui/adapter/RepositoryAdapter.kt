package com.fgsguedes.githubrepos.ui.adapter

import android.content.Context
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind
import com.fgsguedes.githubrepos.data.GitHubRepository

class RepositoryAdapter(
    context: Context,
    private val repositories: List<GitHubRepository>
) : RecyclerView.Adapter<RepositoryViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = inflater.inflate(R.layout.list_element_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount() = repositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        with(repositories[position]) {
            holder.name.text = name
            holder.description.text = description

            holder.language.text = language
            holder.language.visibility = when (language) {
                null -> View.GONE
                else -> View.VISIBLE
            }

            holder.starCount.text = stargazers_count.toString()
            holder.forkCount.text = forks_count.toString()

            holder.license.text = licence?.name
            holder.licenseGroup.visibility = when (licence) {
                null -> View.GONE
                else -> View.VISIBLE
            }
        }
    }
}

class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView by bind(R.id.repository_element_name)
    val description: TextView by bind(R.id.repository_element_description)
    val language: TextView by bind(R.id.repository_element_language)
    val starCount: TextView by bind(R.id.repository_element_star_count)
    val forkCount: TextView by bind(R.id.repository_element_forked_count)
    val license: TextView by bind(R.id.repository_element_license_name)
    val licenseGroup: Group by bind(R.id.repository_element_license_group)
}
