package com.fgsguedes.githubrepos.data.database.model

import com.fgsguedes.githubrepos.model.License
import com.fgsguedes.githubrepos.model.Repository
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmRepository(
    @PrimaryKey var id: Long = 0,
    var name: String = "",
    var description: String? = null,
    var language: String? = null,
    var stars: Int = 0,
    var forks: Int = 0,
    var license: String? = null
) : RealmObject() {

    constructor(repository: Repository) : this(
        repository.id,
        repository.name,
        repository.description,
        repository.language,
        repository.starCount,
        repository.forkCount,
        repository.license?.name
    )

    fun toModel() = Repository(
        id, name, description, language, stars, forks,
        license?.let(::License)
    )
}
