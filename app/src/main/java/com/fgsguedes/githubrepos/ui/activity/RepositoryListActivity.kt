package com.fgsguedes.githubrepos.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
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

    private val recyclerView: RecyclerView by bind(R.id.repository_list_recycler_view)
    private val loading: ProgressBar by bind(R.id.repository_list_scroll_loading)

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
        adapter = RepositoryAdapter(this, initialState)

        recyclerView.adapter = adapter
        recyclerView.addOnChildAttachStateChangeListener(listener)

        render(initialState)
    }

    override fun render(newState: RepositoryListState) {
        adapter.update(newState)

        loading.visible = newState.isLoading

        if (newState.loadedEverything) {
            recyclerView.removeOnChildAttachStateChangeListener(listener)
        }
    }

    private val listener: RecyclerView.OnChildAttachStateChangeListener =
        object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View?) {
                // Do nothing
            }

            override fun onChildViewAttachedToWindow(view: View?) {
                val id = view?.tag as? Long
                if (id != null) presenter.onElementDisplayed(id)
            }
        }
}
