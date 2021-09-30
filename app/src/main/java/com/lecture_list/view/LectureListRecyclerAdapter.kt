package com.lecture_list.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lecture_list.databinding.MainRecyclerCellBinding

class LectureListRecyclerAdapter(
    private val context: Context,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<LectureListRecyclerAdapter.ViewHolder>() {
    private lateinit var recyclerBinding: MainRecyclerCellBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        recyclerBinding =
            MainRecyclerCellBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(recyclerBinding.root)
    }

    override fun getItemCount(): Int {
        return viewModel.lectureListItem.value?.count() ?: 0
    }

    private fun setCheckedState(position: Int, checked: Boolean) {
        var value = viewModel.lectureListItem.value ?: return
        value[position].bookmarked = checked
        viewModel.lectureListItem.accept(value)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.lectureListItem?.value?.get(position) ?: return
        recyclerBinding.courseInfo = item
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
