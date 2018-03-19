package com.fgsguedes.githubrepos.tests.util

import com.fgsguedes.githubrepos.model.License
import com.fgsguedes.githubrepos.model.Repository

object Fabricator {

    fun repository(
        id: Long = 1,
        name: String = "Foo",
        description: String? = "Just any default data",
        language: String? = "Kotlin",
        starCount: Int = 42,
        forkCount: Int = 13,
        license: License? = License("Apache")
    ) = Repository(id, name, description, language, starCount, forkCount, license)

    fun repositoryList(size: Long) = (1..size).map {
        repository(id = it, name = "Foo $it")
    }.toList()
}
