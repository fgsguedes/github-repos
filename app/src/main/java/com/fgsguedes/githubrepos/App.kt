package com.fgsguedes.githubrepos

import android.app.Activity
import android.app.Application
import com.fgsguedes.githubrepos.di.DaggerApplicationComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        DaggerApplicationComponent.builder()
            .application(this)
            .build()
            .injectMembers(this)
    }

    override fun activityInjector() = activityInjector
}
