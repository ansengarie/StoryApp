package com.ansengarie.storyapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ansengarie.storyapp.data.response.StoryResponse

class StoryCallback(
    private val mOldNoteList: List<StoryResponse>,
    private val mNewNoteList: List<StoryResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldNoteList.size
    }

    override fun getNewListSize(): Int {
        return mNewNoteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldNoteList[oldItemPosition].id == mNewNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldNoteList[oldItemPosition]
        val newEmployee = mNewNoteList[newItemPosition]
        return oldEmployee.id == newEmployee.id && oldEmployee.photoUrl == newEmployee.photoUrl
    }

}