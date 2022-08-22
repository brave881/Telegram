package com.example.telegram.ui.fragments.single_chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.databinding.FragmentSingleChatBinding
import com.example.telegram.models.CommonModel
import com.example.telegram.models.User
import com.example.telegram.ui.fragments.BaseFragment
import com.example.telegram.utilites.*
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {
    private lateinit var mInfoToolbar: View
    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: User
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mBinding: FragmentSingleChatBinding
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRefMessages: DatabaseReference
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mMessageListener: AppChildEventListener
    private var mCountMessages = 10
    private var mIsScrolling = false
    private var mSmoothScrollToPosition = true
    private var mListListeners = mutableListOf<AppChildEventListener>()


    override fun onResume() {
        super.onResume()
        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = mBinding.rvSingleChat
        mAdapter = SingleChatAdapter()
        mRefMessages =
            REF_DATABASE_ROOT
                .child(NODE_MESSAGES)
                .child(CURRENT_UID)
                .child(contact.id)
        mRecyclerView.adapter = mAdapter

        mMessageListener = AppChildEventListener { snapshot ->
            mAdapter.addItem(snapshot.getCommonModel())
            if (mSmoothScrollToPosition) {
                mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
            }
        }
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessageListener)
        mListListeners.add(mMessageListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }
        })


    }

    private fun updateData() {
        mIsScrolling = false
        mSmoothScrollToPosition = false
        mCountMessages += 10
        mRefMessages.limitToLast(mCountMessages).addChildEventListener(mMessageListener)
        mListListeners.add(mMessageListener)
    }

    private fun initToolbar() {
        mInfoToolbar = APP_ACTIVITY.mToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
        mInfoToolbar.visibility = View.VISIBLE
        mListenerInfoToolbar = AppValueEventListener {
            mReceivingUser = it.getUser()
            initInfoToolbar()
        }
        mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefUsers.addValueEventListener(mListenerInfoToolbar)

        mBinding.btnSendMessage.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = mBinding.etMessageField.text.toString()
            if (message.isEmpty()) {
                showToast("Input message!")
            } else sendMessage(message, contact.id, TYPE_TEXT) {
                mBinding.etMessageField.setText("")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    private fun initInfoToolbar() {
        mInfoToolbar.findViewById<CircleImageView>(R.id.img_toolbar_chat)
            .photoDownloadAndSet(mReceivingUser.photoUrl)
        if (mReceivingUser.fullname.isEmpty()) {
            mInfoToolbar.findViewById<TextView>(R.id.tv_toolbar_name).text = contact.fullname
        } else mInfoToolbar.findViewById<TextView>(R.id.tv_toolbar_name).text =
            mReceivingUser.fullname

        mInfoToolbar.findViewById<TextView>(R.id.tv_toolbar_status).text = mReceivingUser.state


    }

    override fun onPause() {
        super.onPause()
        mInfoToolbar.visibility = View.GONE
        mRefUsers.removeEventListener(mListenerInfoToolbar)
        mRefMessages.removeEventListener(mMessageListener)
        mListListeners.forEach {
            mRefMessages.removeEventListener(it)
        }
    }
}