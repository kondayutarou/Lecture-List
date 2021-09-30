package com.lecture_list.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay3.BehaviorRelay
import com.lecture_list.databinding.MainRecyclerCellBinding
import com.lecture_list.model.LectureListItem

class LectureListRecyclerAdapter(
    private val context: Context,
    private val lectureList: BehaviorRelay<MutableList<LectureListItem>>
) : RecyclerView.Adapter<LectureListRecyclerAdapter.ViewHolder>() {
    private lateinit var recyclerBinding: MainRecyclerCellBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        recyclerBinding = MainRecyclerCellBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(recyclerBinding.root)
    }

    override fun getItemCount(): Int {
        return lectureList.value.count()
    }

    private fun setCheckedState(position: Int, checked: Boolean) {
        var value = lectureList.value
        value[position].bookmarked = checked
        lectureList.accept(value)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lectureList.value[position]
        recyclerBinding.courseInfo = item
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
