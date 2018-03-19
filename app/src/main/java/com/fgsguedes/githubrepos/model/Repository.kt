package com.fgsguedes.githubrepos.model

import com.squareup.moshi.Json

data class Repository(
    val id: Long,
    val name: String,
    val description: String?,
    val language: String?,
    @Json(name = "stargazers_count") val starCount: Int,
    @Json(name = "forks_count") val forkCount: Int,
    val license: License?
)
