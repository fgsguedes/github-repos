package com.fgsguedes.githubrepos.data.repository

import com.fgsguedes.githubrepos.data.api.GitHubApi
import com.fgsguedes.githubrepos.data.database.model.RealmRepository
import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.Maybe
import io.realm.Realm
import io.realm.kotlin.where
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class RepositoriesRepository @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val realmProvider: Provider<Realm>
) {

    private var hasNextPage = true

    fun list(page: Int): Maybe<Response> {
        if (!hasNextPage) return Maybe.empty()

        return gitHubApi.repositories(page = page)
            .flatMapMaybe { result ->
                val response = result.response()
                val headers = response?.headers()
                val body = response?.body()

                hasNextPage = result.isError || headers?.get("Link")
                    ?.contains("rel=\"next\"") ?: false

                when {
                    result.isError -> Maybe.error(result.error())
                    body != null -> Maybe.just(Response(body, hasNextPage))
                    else -> Maybe.empty()
                }
            }
            .doOnSuccess(::cacheRepositories)
            .onErrorResumeNext(::switchToCachedIfIoError)
    }

    private fun cacheRepositories(response: Response) {
        realmProvider.get().executeTransaction { realm ->
            response.repositories.forEach {
                val reamObject = RealmRepository(it)
                realm.copyToRealmOrUpdate(reamObject)
            }
        }
    }

    private fun switchToCachedIfIoError(error: Throwable): Maybe<Response> {
        return if (error is IOException) {
            val repositories = realmProvider.get()
                .where<RealmRepository>()
                .findAll()
                .map(RealmRepository::toModel)

            Maybe.just(Response(repositories, cached = true))

        } else Maybe.error(error)
    }

    data class Response(
        val repositories: List<Repository>,
        val hasNextPage: Boolean = true,
        val cached: Boolean = false
    )
}
