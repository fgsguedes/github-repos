package com.fgsguedes.githubrepos

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View

fun <T : View> AppCompatActivity.bind(@IdRes viewId: Int) = lazy { findViewById<T>(viewId) }
