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
            holder.language.visible = !language.isNullOrBlank()

            holder.starCount.text = stargazers_count.toString()
            holder.starGroup.visible = stargazers_count > 0

            holder.forkCount.text = forks_count.toString()
            holder.forkGroup.visible = forks_count > 0

            holder.license.text = license?.name
            holder.licenseGroup.visible = license != null
        }
    }

    private var View.visible: Boolean
        get() = visibility == View.VISIBLE
        set(value) {
            visibility = if (value) View.VISIBLE
            else View.GONE
        }
}

class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView by bind(R.id.repository_element_name)
    val description: TextView by bind(R.id.repository_element_description)
    val language: TextView by bind(R.id.repository_element_language)
    val starCount: TextView by bind(R.id.repository_element_star_count)
    val starGroup: Group by bind(R.id.repository_element_star_group)
    val forkCount: TextView by bind(R.id.repository_element_forked_count)
    val forkGroup: Group by bind(R.id.repository_element_forked_group)
    val license: TextView by bind(R.id.repository_element_license_name)
    val licenseGroup: Group by bind(R.id.repository_element_license_group)
}
