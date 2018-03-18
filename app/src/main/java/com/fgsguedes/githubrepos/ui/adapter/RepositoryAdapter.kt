package com.fgsguedes.githubrepos.ui.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.presenter.RepositoryListState
import com.fgsguedes.githubrepos.ui.adapter.viewholder.RepositoryViewHolder
import com.fgsguedes.githubrepos.visible

class RepositoryAdapter(
    context: Context,
    private var state: RepositoryListState
) : RecyclerView.Adapter<RepositoryViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = inflater.inflate(R.layout.list_element_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount() = state.repositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
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

    fun update(newState: RepositoryListState) {
        if (state == newState) return

        val diffCallback = StateDiff(state, newState)
        val diff = DiffUtil.calculateDiff(diffCallback)

        state = newState
        diff.dispatchUpdatesTo(this)
    }
}

class StateDiff(
    private val oldState: RepositoryListState,
    private val newState: RepositoryListState
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldState.repositories.size
    override fun getNewListSize() = newState.repositories.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldState.repositories[oldItemPosition] === newState.repositories[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldState.repositories[oldItemPosition] == newState.repositories[newItemPosition]
    }
}
