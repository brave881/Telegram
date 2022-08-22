package com.example.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.telegram.R
import com.example.telegram.databinding.FragmentChangeBioBinding
import com.example.telegram.utilites.*


class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {
    private lateinit var binding: FragmentChangeBioBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeBioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.etChangeBio.setText(USER.bio)
    }

    override fun change() {
        val bio = binding.etChangeBio.text.toString()
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO).setValue(bio)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.update_data_toastt))
                    USER.bio = bio
                    fragmentManager?.popBackStack()
                }
            }
    }

}