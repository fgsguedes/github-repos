package com.fgsguedes.githubrepos.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("/users/{username}/repos")
    fun repositories(
        @Path("username") username: String = "JakeWharton",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15
    ): Observable<List<GitHubRepository>>
}

data class GitHubRepository(
    val name: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val licence: GitHubLicense?
)

data class GitHubLicense(
    val name: String
)