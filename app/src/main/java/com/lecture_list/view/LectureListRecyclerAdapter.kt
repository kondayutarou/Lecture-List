package com.lecture_list.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lecture_list.databinding.MainRecyclerCellBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_recycler_cell.view.*

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
        return viewModel.lectureListForView.value?.count() ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.lectureListForView?.value?.get(position) ?: return

        Picasso.get()
            .load(item.iconUrl)
            .into(holder.itemView.teacher_image)

        recyclerBinding.courseInfo = item
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
