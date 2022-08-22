package com.example.telegram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.telegram.databinding.ActivityMainBinding
import com.example.telegram.ui.fragments.EnterPhoneNumberFragment
import com.example.telegram.ui.fragments.MainFragment
import com.example.telegram.ui.objects.AppDrawer
import com.example.telegram.utilites.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mbinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer


    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunctions()
        }
    }


    private fun initFunctions() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), false)
        }
    }


    private fun initFields() {
        mToolbar = mbinding.toolbarMain
        mAppDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = "Telegram"
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Telegram"
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY, READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }

}

/* **+1 650-555-1212** */