package com.fgsguedes.githubrepos.presenter

import com.fgsguedes.githubrepos.RepositoriesRepository
import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(
    private val view: RepositoryListView,
    private val repositories: RepositoriesRepository
) {

    private var currentPage = 1
    private var currentState = RepositoryListState()

    fun onCreate() {
        view.setUp(currentState)
        loadPage(currentPage)
    }

    fun onElementDisplayed(id: Long) {
        if (currentState.loadedEverything) return

        val repositoryList = currentState.repositories
        val position = repositoryList.indexOfLast { it.id == id }
        if (position == repositoryList.size - 3) loadPage(currentPage + 1)
    }

    private fun loadPage(page: Int) {
        render(currentState.copy(isLoading = true))

        repositories.list(page)
            .delay(5, TimeUnit.SECONDS)
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
            val newState = currentState.copy(
                repositories = currentState.repositories + repositories,
                isLoading = false
            )
            render(newState)
        }
    }

    private fun onListRepositoriesError(throwable: Throwable) {
        val newState = currentState.copy(
            error = throwable,
            isLoading = false
        )
        render(newState)
    }

    private fun onEmptyRepositories() {
        val newState = currentState.copy(
            loadedEverything = true,
            isLoading = false
        )
        render(newState)
    }

    private fun render(newState: RepositoryListState) {
        currentState = newState
        view.render(newState)
    }
}

data class RepositoryListState(
    val repositories: List<Repository> = emptyList(),
    val loadedEverything: Boolean = false,
    val isLoading: Boolean = true,
    val error: Throwable? = null
)

interface RepositoryListView {
    fun setUp(initialState: RepositoryListState)
    fun render(newState: RepositoryListState)
}
