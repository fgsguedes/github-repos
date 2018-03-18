package com.fgsguedes.githubrepos.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.fgsguedes.githubrepos.App
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind
import com.fgsguedes.githubrepos.data.GitHubRepository
import com.fgsguedes.githubrepos.presenter.RepositoryListPresenter
import com.fgsguedes.githubrepos.presenter.RepositoryListView
import com.fgsguedes.githubrepos.ui.adapter.RepositoryAdapter

class RepositoryListActivity : AppCompatActivity(), RepositoryListView {

    private val recyclerView: RecyclerView by bind(R.id.repository_list_recycler_view)

    private val presenter by lazy {
        RepositoryListPresenter(this, (applicationContext as App).gitHubApi)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        presenter.onCreate()
    }

    override fun showRepositories(repositories: List<GitHubRepository>) {
        recyclerView.adapter = RepositoryAdapter(this, repositories)
    }
}
