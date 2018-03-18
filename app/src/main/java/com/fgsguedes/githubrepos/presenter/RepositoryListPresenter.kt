package com.fgsguedes.githubrepos.presenter

import com.fgsguedes.githubrepos.RepositoriesRepository
import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(
    private val view: RepositoryListView,
    private val repositories: RepositoriesRepository
) {

    fun onCreate() {
        repositories.list()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showRepositories)
    }
}

interface RepositoryListView {
    fun showRepositories(repositories: List<Repository>)
}
