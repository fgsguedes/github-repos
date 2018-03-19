package com.fgsguedes.githubrepos.presenter

import com.fgsguedes.githubrepos.RepositoriesRepository
import com.fgsguedes.githubrepos.RepositoriesRepository.Response
import com.fgsguedes.githubrepos.tests.util.Fabricator
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryListPresenterTest {

    @Mock
    private lateinit var repository: RepositoriesRepository

    private val immediateComposer = MaybeTransformer { upstream: Maybe<RepositoryListState> ->
        upstream.observeOn(Schedulers.trampoline())
            .subscribeOn(Schedulers.trampoline())
    }

    private lateinit var presenter: RepositoryListPresenter

    @Before
    fun setUp() {
        presenter = RepositoryListPresenter(repository, immediateComposer)
    }

    @Test
    fun `state shows response and is not loading`() {
        val mockedResponse = Response(
            Fabricator.repositoryList(5)
        )

        whenever(repository.list(anyInt()))
            .thenReturn(Maybe.just(mockedResponse))

        val testObserver = presenter.viewState().test()

        presenter.onCreate()

        testObserver.assertValues(
            RepositoryListState(),
            RepositoryListState(repositories = mockedResponse.repositories, isLoading = false)
        )

        verify(repository, times(1)).list(1)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `state appends second loaded page`() {
        val firstResponse = Response(Fabricator.repositoryList(1))
        val secondResponse = Response(Fabricator.repositoryList(2))

        whenever(repository.list(1)).thenReturn(Maybe.just(firstResponse))
        whenever(repository.list(2)).thenReturn(Maybe.just(secondResponse))

        val testObserver = presenter.viewState().test()

        presenter.onCreate()
        presenter.loadMore()

        testObserver.assertValues(
            RepositoryListState(),
            RepositoryListState(repositories = firstResponse.repositories, isLoading = false),
            RepositoryListState(repositories = firstResponse.repositories, isLoading = true),
            RepositoryListState(
                repositories = firstResponse.repositories + secondResponse.repositories,
                isLoading = false
            )
        )

        verify(repository, times(1)).list(1)
        verify(repository, times(1)).list(2)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `do nothing when trying to load more after last page was loaded`() {
        val mockedResponse = Response(
            Fabricator.repositoryList(1),
            hasNextPage = false
        )

        whenever(repository.list(1)).thenReturn(Maybe.just(mockedResponse))

        val testObserver = presenter.viewState().test()

        presenter.onCreate()
        presenter.loadMore()

        testObserver.assertValues(
            RepositoryListState(),
            RepositoryListState(
                repositories = mockedResponse.repositories,
                hasNextPage = false,
                isLoading = false
            )
        )

        verify(repository, times(1)).list(1)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `do nothing if try to retry not cached response`() {
        val mockedResponse = Response(
            Fabricator.repositoryList(1),
            hasNextPage = true,
            cached = false
        )

        whenever(repository.list(1)).thenReturn(Maybe.just(mockedResponse))

        val testObserver = presenter.viewState().test()

        presenter.onCreate()
        presenter.retry()

        testObserver.assertValues(
            RepositoryListState(),
            RepositoryListState(
                repositories = mockedResponse.repositories,
                isLoading = false
            )
        )

        verify(repository, times(1)).list(1)
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `load first page after cached response`() {
        val firstResponse = Response(Fabricator.repositoryList(1))
        val secondResponse = Response(Fabricator.repositoryList(1), cached = true)

        whenever(repository.list(1)).thenReturn(Maybe.just(firstResponse))
        whenever(repository.list(2)).thenReturn(Maybe.just(secondResponse))

        val testObserver = presenter.viewState().test()

        presenter.onCreate()
        presenter.loadMore()
        presenter.retry()

        testObserver.assertValues(
            RepositoryListState(),
            RepositoryListState(repositories = firstResponse.repositories, isLoading = false),
            RepositoryListState(repositories = firstResponse.repositories, isLoading = true),
            RepositoryListState(
                repositories = secondResponse.repositories,
                isLoading = false,
                cached = true
            ),
            RepositoryListState(repositories = emptyList(), isLoading = true),
            RepositoryListState(repositories = firstResponse.repositories, isLoading = false)
        )

        verify(repository, times(2)).list(1)
        verify(repository, times(1)).list(2)
        verifyNoMoreInteractions(repository)
    }
}
