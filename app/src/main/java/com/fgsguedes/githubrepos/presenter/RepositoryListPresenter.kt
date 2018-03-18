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

    private var nextPage = 1
    private var reachedEnd = false

    fun onCreate() {
        nextPage()
    }

    fun nextPage() {
        if (reachedEnd) return

        repositories.list(nextPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::onReceivedRepositories,
                ::onListRepositoriesError,
                ::onEmptyRepositories
            )
    }

    private fun onReceivedRepositories(repositories: List<Repository>) {
        nextPage++
        if (repositories.isNotEmpty()) view.showRepositories(repositories)
    }

    private fun onListRepositoriesError(throwable: Throwable) {
        TODO("not implemented")
    }

    private fun onEmptyRepositories() {
        reachedEnd = true
    }
}

interface RepositoryListView {
    fun showRepositories(repositories: List<Repository>)
}
