package com.example.telegram.ui.fragments

import androidx.fragment.app.Fragment
import com.example.telegram.utilites.APP_ACTIVITY

open class BaseFragment(private val layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

}
