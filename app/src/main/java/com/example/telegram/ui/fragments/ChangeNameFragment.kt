package com.example.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telegram.R
import com.example.telegram.databinding.FragmentChangeNameBinding
import com.example.telegram.utilites.*


class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
    private lateinit var binding: FragmentChangeNameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initFullNameList()

    }

    private fun initFullNameList() {
        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {
            binding.etChangeName.setText(fullNameList[0])
            binding.etChangeSurname.setText(fullNameList[1])
        } else binding.etChangeName.setText(fullNameList[0])
    }


    override fun change() {
        val name = binding.etChangeName.text.toString()
        val surname = binding.etChangeSurname.text.toString()
        if (name.isEmpty()) {
            showToast("Name can't be blank")
        } else {
            val fullname = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULL_NAME)
                .setValue(fullname).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.update_data_toastt))
                        USER.fullname = fullname
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                        APP_ACTIVITY.supportFragmentManager.popBackStack()
                    }
                }
        }
    }


}