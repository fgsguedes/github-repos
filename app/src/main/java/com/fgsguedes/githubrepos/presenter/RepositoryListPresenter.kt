package com.fgsguedes.githubrepos.presenter

import com.fgsguedes.githubrepos.RepositoriesRepository
import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.MaybeTransformer
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class RepositoryListPresenter @Inject constructor(
    private val repositories: RepositoriesRepository,
    private val schedulerComposer: MaybeTransformer<RepositoryListState, RepositoryListState>
) {

    private var nextPage = 1
    private val stateSubject = BehaviorSubject.createDefault(RepositoryListState())

    fun onCreate() {
        fetch(nextPage)
    }

    fun viewState(): Observable<RepositoryListState> = stateSubject

    fun loadMore() {
        if (!stateSubject.value.hasNextPage) return

        stateSubject.onNextCopy { state ->
            state.copy(isLoading = true)
        }
        fetch(nextPage)
    }

    fun retry() {
        if (!stateSubject.value.cached) return

        stateSubject.onNextCopy { state ->
            state.copy(
                repositories = emptyList(),
                cached = false,
                isLoading = true
            )
        }

        nextPage = 1
        fetch(nextPage)
    }

    private fun fetch(page: Int) {
        repositories.list(page)
            .doOnSuccess { if (!it.cached) nextPage++ }
            .map(::repositoryList)
            .map(::toState)
            .compose(schedulerComposer)
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
