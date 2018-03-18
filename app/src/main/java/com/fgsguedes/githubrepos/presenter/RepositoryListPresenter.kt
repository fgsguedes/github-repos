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

    private var currentPage = 1
    private var reachedEnd = false
    private val repositoryList = mutableListOf<Repository>()

    fun onCreate() {
        loadPage(currentPage)
    }

    fun onElementDisplayed(id: Long) {
        if (reachedEnd) return

        val position = repositoryList.indexOfLast { it.id == id }
        if (position == repositoryList.size - 3) loadPage(currentPage + 1)
    }

    fun loadPage(page: Int) {
        repositories.list(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::onReceivedRepositories,
                ::onListRepositoriesError,
                ::onEmptyRepositories
            )
    }

    private fun onReceivedRepositories(repositories: List<Repository>) {
        currentPage++
        if (repositories.isNotEmpty()) {
            repositoryList.addAll(repositories)
            view.showRepositories(repositories)
        }
    }

    private fun onListRepositoriesError(throwable: Throwable) {
        TODO("not implemented")
    }

    private fun onEmptyRepositories() {
        reachedEnd = true
        view.foo()
    }
}

interface RepositoryListView {
    fun showRepositories(repositories: List<Repository>)
    fun foo()
}
