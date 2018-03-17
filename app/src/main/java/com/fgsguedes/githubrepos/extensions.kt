package com.fgsguedes.githubrepos

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View

fun <T : View> AppCompatActivity.bind(@IdRes viewId: Int) = lazy {
    findViewById<T>(viewId)
}

fun <T : View> RecyclerView.ViewHolder.bind(@IdRes viewId: Int) = lazy {
    itemView.findViewById<T>(viewId)
}
