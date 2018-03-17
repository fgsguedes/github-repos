package com.fgsguedes.githubrepos.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.fgsguedes.githubrepos.App
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind
import com.fgsguedes.githubrepos.ui.adapter.RepositoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RepositoryListActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by bind(R.id.repository_list_recycler_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        (applicationContext as App).gitHubApi.repositories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ repositories ->
                recyclerView.adapter = RepositoryAdapter(this, repositories)
            })
    }
}
