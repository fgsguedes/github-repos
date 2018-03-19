package com.fgsguedes.githubrepos.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind
import com.fgsguedes.githubrepos.presenter.RepositoryListPresenter
import com.fgsguedes.githubrepos.presenter.RepositoryListState
import com.fgsguedes.githubrepos.presenter.RepositoryListView
import com.fgsguedes.githubrepos.ui.adapter.RepositoryAdapter
import com.fgsguedes.githubrepos.visible
import dagger.android.AndroidInjection
import javax.inject.Inject

class RepositoryListActivity : AppCompatActivity(), RepositoryListView {

    private val cachedWarning: TextView by bind(R.id.repository_list_connection_warning)
    private val recyclerView: RecyclerView by bind(R.id.repository_list_recycler_view)

    private lateinit var adapter: RepositoryAdapter

    @Inject
    lateinit var presenter: RepositoryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        AndroidInjection.inject(this)

        presenter.onCreate()
    }

    override fun setUp(initialState: RepositoryListState) {
        adapter = RepositoryAdapter(this, initialState, presenter::loadMore)

        cachedWarning.setOnClickListener { presenter.retry() }

        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        render(initialState)
    }

    override fun render(newState: RepositoryListState) {
        cachedWarning.visible = newState.cached
        adapter.update(newState)
    }
}
