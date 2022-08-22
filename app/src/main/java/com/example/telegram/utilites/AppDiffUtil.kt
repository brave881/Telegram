package com.example.telegram.utilites

import androidx.recyclerview.widget.DiffUtil
import com.example.telegram.models.CommonModel

class AppDiffUtil(var oldList: List<CommonModel>, var newList: List<CommonModel>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].timeStamp == newList[newItemPosition].timeStamp

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}