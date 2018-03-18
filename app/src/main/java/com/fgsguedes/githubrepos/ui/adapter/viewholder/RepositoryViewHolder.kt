package com.fgsguedes.githubrepos.ui.adapter.viewholder

import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.fgsguedes.githubrepos.R
import com.fgsguedes.githubrepos.bind

class RepositoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView by bind(R.id.repository_element_name)
    val description: TextView by bind(R.id.repository_element_description)
    val language: TextView by bind(R.id.repository_element_language)
    val starCount: TextView by bind(R.id.repository_element_star_count)
    val starGroup: Group by bind(R.id.repository_element_star_group)
    val forkCount: TextView by bind(R.id.repository_element_forked_count)
    val forkGroup: Group by bind(R.id.repository_element_forked_group)
    val license: TextView by bind(R.id.repository_element_license_name)
    val licenseGroup: Group by bind(R.id.repository_element_license_group)
}
