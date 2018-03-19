package com.fgsguedes.githubrepos.di

import com.fgsguedes.githubrepos.App
import dagger.BindsInstance
import dagger.Component
import dagger.MembersInjector
import dagger.android.AndroidInjectionModule

@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        ReactiveModule::class,
        NetworkingModule::class,
        DatabaseModule::class
    ]
)
interface ApplicationComponent : MembersInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): ApplicationComponent
    }
}
