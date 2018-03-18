package com.fgsguedes.githubrepos

import com.fgsguedes.githubrepos.data.api.GitHubApi
import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.Maybe
import javax.inject.Inject

class RepositoriesRepository @Inject constructor(
    private val gitHubApi: GitHubApi
) {

    private var hasNextPage = true

    fun list(page: Int): Maybe<List<Repository>> {
        if (!hasNextPage) return Maybe.empty()

        return gitHubApi.repositories(page = page)
            .flatMapMaybe { result ->
                val response = result.response()
                val headers = response?.headers()
                val body = response?.body()

                hasNextPage = headers?.get("Link")
                    ?.contains("rel=\"next\"") ?: false

                when {
                    result.isError -> Maybe.error(result.error())
                    body != null -> Maybe.just(body)
                    else -> Maybe.empty()
                }
            }
    }
}
