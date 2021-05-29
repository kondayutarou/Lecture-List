package com.shoppinglist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay3.BehaviorRelay
import com.shoppinglist.databinding.MainRecyclerCellBinding
import com.shoppinglist.data.ShoppingListItem
import kotlinx.android.synthetic.main.main_recycler_cell.view.*

class MainRecyclerViewAdapter(
    private val context: Context,
    private val shoppingList: BehaviorRelay<MutableList<ShoppingListItem>>
) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MainRecyclerCellBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return shoppingList.value.count()
    }

    private fun setCheckedState(position: Int, checked: Boolean) {
        var value = shoppingList.value
        value[position].checked = checked
        shoppingList.accept(value)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shoppingList.value[position]
        holder.itemView.apply {
            textView.text = item.name
            checkBox.isChecked = item.checked
            checkBox.setOnCheckedChangeListener { _, boolean ->
                setCheckedState(position, boolean)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}