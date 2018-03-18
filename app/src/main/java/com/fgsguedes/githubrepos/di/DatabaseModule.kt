package com.fgsguedes.githubrepos.di

import dagger.Module
import dagger.Provides
import io.realm.Realm

@Module
class DatabaseModule {

    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}
