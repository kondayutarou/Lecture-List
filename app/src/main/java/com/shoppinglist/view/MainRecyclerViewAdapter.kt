package com.shoppinglist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shoppinglist.ShoppingListItem
import com.shoppinglist.databinding.MainRecyclerCellBinding
import kotlinx.android.synthetic.main.main_recycler_cell.view.*

class MainRecyclerViewAdapter(
    private val context: Context,
    private val shoppingList: List<ShoppingListItem>
) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MainRecyclerCellBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return shoppingList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shoppingList[position]
        holder.itemView.apply {
            textView.text = item.name
            checkBox.isChecked = item.checked
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}