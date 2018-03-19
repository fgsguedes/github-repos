package com.fgsguedes.githubrepos.di

import com.fgsguedes.githubrepos.ui.activity.RepositoryListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun repositoryListActivity(): RepositoryListActivity
}
