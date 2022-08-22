package com.example.telegram.ui.objects

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.telegram.R
import com.example.telegram.ui.fragments.ContactFragment
import com.example.telegram.ui.fragments.SettingsFragment
import com.example.telegram.utilites.APP_ACTIVITY
import com.example.telegram.utilites.USER
import com.example.telegram.utilites.photoDownloadAndSet
import com.example.telegram.utilites.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer {
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mCurrentProfile: ProfileDrawerItem
    fun create() {
        createHeader()
        createDrawer()
        initLoader()
        mDrawerLayout = mDrawer.drawerLayout
    }


    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        APP_ACTIVITY.mToolbar.setNavigationOnClickListener {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        APP_ACTIVITY.mToolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(APP_ACTIVITY)
            .withToolbar(APP_ACTIVITY.mToolbar)
            .withAccountHeader(mHeader)
            .withSelectedItem(-1)
            .withActionBarDrawerToggle(true)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(100)
                    .withName("Add Account")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_baseline_add_24),
                PrimaryDrawerItem()
                    .withIdentifier(101)
                    .withName("Create group")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_create_groups),
                PrimaryDrawerItem()
                    .withIdentifier(102)
                    .withName("Contacts")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_contacts),
                PrimaryDrawerItem()
                    .withIdentifier(103)
                    .withName("Calls")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_phone),
                PrimaryDrawerItem()
                    .withIdentifier(104)
                    .withName("People Nearby")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_nearby),
                PrimaryDrawerItem()
                    .withIdentifier(105)
                    .withName("Saved Messages")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_favorites),
                PrimaryDrawerItem()
                    .withIdentifier(106)
                    .withName("Settings")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_settings),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(107)
                    .withName("Invite Friends")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_invate),
                PrimaryDrawerItem()
                    .withIdentifier(108)
                    .withName("Telegram Features")
                    .withIconTintingEnabled(true)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_help),

                ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    changeFragment(position)


                    return false
                }

            })
            .build()
    }


    fun changeFragment(position: Int) {
        when (position) {
            7 -> replaceFragment(SettingsFragment())
            3 -> replaceFragment(ContactFragment())
            else -> {}
        }
    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITY)
            .withHeaderBackground(R.drawable.header_background)
            .addProfiles(
                mCurrentProfile
            ).build()
    }

    fun updateHeader() {
        mCurrentProfile
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)

        mHeader.updateProfile(mCurrentProfile)
    }


    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            @Deprecated(
                "", ReplaceWith(
                    "imageView.photoDownloadAndSet(uri.toString())",
                    "com.example.telegram.utilites.photoDownloadAndSet"
                )
            )
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.photoDownloadAndSet(uri.toString())
            }
        })
    }
}