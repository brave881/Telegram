package com.example.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telegram.R
import com.example.telegram.databinding.FragmentChangeUsernameBinding
import com.example.telegram.utilites.*


class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {
    private lateinit var binding: FragmentChangeUsernameBinding
    private lateinit var newUsername: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeUsernameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.etChangeUsername.setText(USER.username)
    }


    override fun change() {
        newUsername = binding.etChangeUsername.text.toString().lowercase()
        if (newUsername.isEmpty()) {
            showToast("Field empty")
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(newUsername)) {
                        showToast("username already exists")
                    } else changeUsername()
                })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(newUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
            .setValue(newUsername)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.update_data_toastt))
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(USER.username)
            .removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.update_data_toastt))
                    APP_ACTIVITY.supportFragmentManager.popBackStack()
                    USER.username = newUsername
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}