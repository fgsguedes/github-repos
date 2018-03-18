package com.fgsguedes.githubrepos.di

import com.fgsguedes.githubrepos.ui.activity.RepositoryListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [RepositoryListModule::class])
    abstract fun repositoryListActivity(): RepositoryListActivity
}
