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
import com.fgsguedes.githubrepos.model.Repository
import com.fgsguedes.githubrepos.visible

class RepositoryAdapter(
    context: Context
) : RecyclerView.Adapter<RepositoryViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val repositories = mutableListOf<Repository>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = inflater.inflate(R.layout.list_element_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount() = repositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        with(repositories[position]) {
            holder.itemView.tag = id

            holder.name.text = name

            holder.description.text = description
            holder.description.visible = !description.isNullOrBlank()

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

    fun setElements(repositories: List<Repository>) {
        this.repositories.apply {
            clear()
            addAll(repositories)
        }
        notifyDataSetChanged()
    }

    fun appendElements(repositories: List<Repository>) {
        val oldSize = this.repositories.size
        this.repositories.addAll(repositories)
        notifyItemRangeChanged(oldSize, repositories.size)
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
