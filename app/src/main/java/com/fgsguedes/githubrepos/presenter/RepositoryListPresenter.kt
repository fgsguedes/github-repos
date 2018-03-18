package com.fgsguedes.githubrepos.presenter

import com.fgsguedes.githubrepos.data.GitHubApi
import com.fgsguedes.githubrepos.data.GitHubRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RepositoryListPresenter(
    private val view: RepositoryListView,
    private val gitHubApi: GitHubApi
) {

    fun onCreate() {
        gitHubApi.repositories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showRepositories)
    }
}

interface RepositoryListView {
    fun showRepositories(repositories: List<GitHubRepository>)
}
