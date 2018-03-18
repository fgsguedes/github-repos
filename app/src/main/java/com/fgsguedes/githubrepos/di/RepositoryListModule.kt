package com.fgsguedes.githubrepos.di

import com.fgsguedes.githubrepos.presenter.RepositoryListView
import com.fgsguedes.githubrepos.ui.activity.RepositoryListActivity
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryListModule {

    @Binds
    abstract fun activity(activity: RepositoryListActivity): RepositoryListView
}
