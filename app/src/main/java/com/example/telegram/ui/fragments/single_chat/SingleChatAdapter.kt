package com.example.telegram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.telegram.R
import com.example.telegram.models.CommonModel
import com.example.telegram.utilites.AppDiffUtil
import com.example.telegram.utilites.CURRENT_UID
import com.example.telegram.utilites.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<CommonModel>()
    private lateinit var mDiffUtil: DiffUtil.DiffResult

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blocUserMessage: ConstraintLayout = view.findViewById(R.id.chat_user_message_block)
        val chatUserMessage: TextView = view.findViewById(R.id.tv_message_item)
        val chatUserMessageTime: TextView = view.findViewById(R.id.tv_message_item_time)

        val blocReceivedMessage: ConstraintLayout =
            view.findViewById(R.id.chat_received_message_block)
        val chatReceivedMessage: TextView = view.findViewById(R.id.tv_message_received_item)
        val chatReceivedMessageTime: TextView =
            view.findViewById(R.id.tv_message_received_item_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blocUserMessage.visibility = View.VISIBLE
            holder.blocReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blocUserMessage.visibility = View.GONE
            holder.blocReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timeStamp.toString().asTime()
        }
    }


    fun addItem(item: CommonModel) {
        val newList = mutableListOf<CommonModel>()
        newList.addAll(mListMessagesCache)
        if (!newList.contains(item)) newList.add(item)
        newList.sortBy { it.timeStamp.toString() }
        mDiffUtil = DiffUtil.calculateDiff(AppDiffUtil(mListMessagesCache, newList))
        mDiffUtil.dispatchUpdatesTo(this)
        mListMessagesCache = newList

    }
}


