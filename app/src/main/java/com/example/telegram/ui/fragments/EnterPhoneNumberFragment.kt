package com.example.telegram.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.telegram.R
import com.example.telegram.databinding.FragmentEnterPhoneNumberBinding
import com.example.telegram.utilites.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mBinding: FragmentEnterPhoneNumberBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Добро пожаловать")
                        restartActivity()
                    } else showToast(task.exception?.message.toString())
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0.message.toString())
                Log.d("TAG", "onVerificationFailed: ${p0.message.toString()}")
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
                Log.d("TAG", "onCodeSent: $id")

            }
        }
        mBinding.btnRegNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if (mBinding.etRegPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }
    }

    private fun authUser() {
        mPhoneNumber = mBinding.etRegPhoneNumber.text.toString()
        /*   val option = PhoneAuthOptions.Builder(AUTH)
               .setPhoneNumber(mPhoneNumber       )
               .setActivity(   APP_ACTIVITY      )
               .setCallbacks(  mCallback         )
               .setTimeout(    60L,
                              TimeUnit.SECONDS      )
               .build()*/

        PhoneAuthProvider.getInstance()
            .verifyPhoneNumber(
                mPhoneNumber,
                60L,
                TimeUnit.SECONDS,
                APP_ACTIVITY,
                mCallback
            )

    }
}