package com.example.telegram.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.databinding.FragmentContactBinding
import com.example.telegram.models.CommonModel
import com.example.telegram.ui.fragments.single_chat.SingleChatFragment
import com.example.telegram.utilites.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference


class ContactFragment : BaseFragment(R.layout.fragment_contact) {

    private lateinit var rv: RecyclerView
    private lateinit var binding: FragmentContactBinding
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactHolder>
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefContacts: DatabaseReference
    private lateinit var mRefUsersListener: AppValueEventListener
    private var mapListener = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Contact"
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initRecyclerView() {
        rv = binding.rvContact

        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts, CommonModel::class.java)
            .build()

        initRecyclerAdapter(options)


    }

    private fun initRecyclerAdapter(options: FirebaseRecyclerOptions<CommonModel>) {
        mAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_items, parent, false)
                return ContactHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()
                    if (contact.fullname.isEmpty())
                        holder.name.text = model.fullname
                    else holder.name.text = contact.fullname
                    holder.state.text = contact.state
                    holder.photo.photoDownloadAndSet(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }
                }

                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListener[mRefUsers] = mRefUsersListener
            }
        }
        rv.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tv_contact_name)
        val state: TextView = view.findViewById(R.id.tv_contact_state)
        val photo: ImageView = view.findViewById(R.id.img_contact_image)

    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()

        mapListener.forEach {
            it.key.removeEventListener(it.value)
        }

    }
}

