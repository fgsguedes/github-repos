package com.fgsguedes.githubrepos

import com.fgsguedes.githubrepos.data.api.GitHubApi
import javax.inject.Inject

class RepositoriesRepository @Inject constructor(
    private val gitHubApi: GitHubApi
) {

    fun list() = gitHubApi.repositories()
}
