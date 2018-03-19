package com.fgsguedes.githubrepos.di

import com.fgsguedes.githubrepos.presenter.RepositoryListState
import dagger.Module
import dagger.Provides
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class ReactiveModule {

    @Provides
    fun provideSchedulerComposer() = MaybeTransformer { upstream: Maybe<RepositoryListState> ->
        upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
