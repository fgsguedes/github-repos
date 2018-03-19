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

    fun loadMore() {
        if (!currentState.hasNextPage) return

        val newState = currentState.copy(
            isLoading = true
        )
        render(newState)
        loadPage(currentPage + 1)
    }

    fun retry() {
        val newState = currentState.copy(
            repositories = emptyList(),
            cached = false,
            isLoading = true
        )
        render(newState)

        currentPage = 1
        loadPage(currentPage)
    }

    private fun loadPage(page: Int) {
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

    private fun onReceivedRepositories(response: RepositoriesRepository.Response) {
        if (!response.cached) currentPage++

        if (response.repositories.isNotEmpty()) {
            val newList = if (response.cached) response.repositories
            else currentState.repositories + response.repositories

            val newState = currentState.copy(
                repositories = newList,
                hasNextPage = response.hasNextPage,
                cached = response.cached,
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
            hasNextPage = false,
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
    val hasNextPage: Boolean = true,
    val isLoading: Boolean = true,
    val cached: Boolean = false,
    val error: Throwable? = null
)

interface RepositoryListView {
    fun setUp(initialState: RepositoryListState)
    fun render(newState: RepositoryListState)
}
