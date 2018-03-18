package com.fgsguedes.githubrepos.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind
import com.fgsguedes.githubrepos.model.Repository
import com.fgsguedes.githubrepos.presenter.RepositoryListPresenter
import com.fgsguedes.githubrepos.presenter.RepositoryListView
import com.fgsguedes.githubrepos.ui.adapter.RepositoryAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

class RepositoryListActivity : AppCompatActivity(), RepositoryListView {

    private val recyclerView: RecyclerView by bind(R.id.repository_list_recycler_view)

    private val adapter by lazy { RepositoryAdapter(this) }

    @Inject
    lateinit var presenter: RepositoryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        AndroidInjection.inject(this)

        setupViews()
        presenter.onCreate()
    }

    override fun showRepositories(repositories: List<Repository>) {
        adapter.appendElements(repositories)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.repository_list_menu_next -> true.also {
                presenter.nextPage()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.repository_list_menu, menu)
        return true
    }

    private fun setupViews() {
        recyclerView.adapter = adapter
    }
}
