package com.fgsguedes.githubrepos.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.presenter.RepositoryListState
import com.fgsguedes.githubrepos.ui.adapter.viewholder.LoadingViewHolder
import com.fgsguedes.githubrepos.ui.adapter.viewholder.RepositoryViewHolder
import com.fgsguedes.githubrepos.visible

class RepositoryAdapter(
    context: Context,
    private var state: RepositoryListState,
    private val loadMoreCallback: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var triggeredLoading = false

    override fun getItemViewType(position: Int): Int {
        return if (position == state.repositories.size) 1
        else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = inflater.inflate(R.layout.list_element_repository, parent, false)
            RepositoryViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.list_element_repository_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount() = state.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (state.hasNextPage && position == state.repositories.size - 3 && !triggeredLoading) {
            triggeredLoading = true
            loadMoreCallback()
        }

        if (holder is RepositoryViewHolder) {
            with(state.repositories[position]) {
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
    }

    fun update(newState: RepositoryListState) {
        if (state == newState) return

        val oldState = state
        state = newState
        if (oldState.repositories != newState.repositories) {
            notifyDataSetChanged()
        }

        if (triggeredLoading) {
            triggeredLoading = newState.isLoading
        }
    }
}

private val RepositoryListState.size: Int
    get() = repositories.size + if (hasNextPage) 1 else 0
