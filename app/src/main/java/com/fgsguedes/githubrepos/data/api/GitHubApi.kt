package com.fgsguedes.githubrepos.data.api

import com.fgsguedes.githubrepos.model.Repository
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("/users/{username}/repos")
    fun repositories(
        @Path("username") username: String = "JakeWharton",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15
    ): Single<Result<List<Repository>>>
}

