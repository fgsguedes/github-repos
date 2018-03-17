package com.fgsguedes.githubrepos.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.fgsguedes.githubrepos.App
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind

class RepositoryListActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by bind(R.id.repository_list_recycler_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        (applicationContext as App).gitHubApi.repositories()
            .subscribe({ repositories ->

            })

    }
}
