package com.fgsguedes.githubrepos.presenter

import com.fgsguedes.githubrepos.RepositoriesRepository
import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(
    private val repositories: RepositoriesRepository
) {

    private var currentPage = 1
    private val stateSubject = BehaviorSubject.createDefault(RepositoryListState())

    fun onCreate() {
        loadPage(currentPage)
    }

    fun viewState(): Observable<RepositoryListState> = stateSubject

    fun loadMore() {
        if (!stateSubject.value.hasNextPage) return

        stateSubject.onNextCopy { state ->
            state.copy(isLoading = true)
        }
        loadPage(currentPage + 1)
    }

    fun retry() {
        stateSubject.onNextCopy { state ->
            state.copy(
                repositories = emptyList(),
                cached = false,
                isLoading = true
            )
        }

        currentPage = 1
        loadPage(currentPage)
    }

    private fun loadPage(page: Int) {
        repositories.list(page)
            .doOnSuccess { if (!it.cached) currentPage++ }
            .map(::repositoryList)
            .map(::toState)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                stateSubject::onNext,
                ::onListRepositoriesError,
                ::onEmptyRepositories
            )
    }

    private fun repositoryList(response: RepositoriesRepository.Response): RepositoriesRepository.Response {
        val newList = if (response.cached) response.repositories
        else stateSubject.value.repositories + response.repositories

        return response.copy(repositories = newList)
    }

    private fun toState(response: RepositoriesRepository.Response): RepositoryListState {
        return stateSubject.value.copy(
            repositories = response.repositories,
            hasNextPage = response.hasNextPage,
            cached = response.cached,
            isLoading = false
        )
    }

    private fun onListRepositoriesError(throwable: Throwable) {
        stateSubject.onNextCopy { state ->
            state.copy(
                error = throwable,
                isLoading = false
            )
        }
    }

    private fun onEmptyRepositories() {
        stateSubject.onNextCopy { state ->
            state.copy(
                hasNextPage = false,
                isLoading = false
            )
        }
    }

    private fun BehaviorSubject<RepositoryListState>.onNextCopy(
        transform: (RepositoryListState) -> RepositoryListState
    ) = onNext(transform(value))
}

data class RepositoryListState(
    val repositories: List<Repository> = emptyList(),
    val hasNextPage: Boolean = true,
    val isLoading: Boolean = true,
    val cached: Boolean = false,
    val error: Throwable? = null
)
