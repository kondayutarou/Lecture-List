package com.lecture_list.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lecture_list.databinding.MainRecyclerCellBinding
import com.lecture_list.model.LectureListItem
import com.squareup.picasso.Picasso

class LectureListRecyclerAdapter(
    private val context: Context,
    private var itemList: ArrayList<LectureListItem>,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<LectureListRecyclerAdapter.ViewHolder>() {
    private lateinit var recyclerBinding: MainRecyclerCellBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        recyclerBinding =
            MainRecyclerCellBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(recyclerBinding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Picasso.get()
            .load(item.iconUrl)
            .into(holder.binding.teacherImage)

        holder.binding.courseInfo = item
        holder.binding.bookmark.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeBookmarkState(isChecked, item)
        }
        if (item.progressError) {
            holder.binding.progressErrorDisplay.visibility = View.VISIBLE
        } else {
            holder.binding.progressErrorDisplay.visibility = View.INVISIBLE
        }

        holder.binding.executePendingBindings()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addItems(itemList: List<LectureListItem>) {
        this.itemList.addAll(itemList)
    }

    fun clear() {
        this.itemList.clear()
    }

    class ViewHolder(val binding: MainRecyclerCellBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
