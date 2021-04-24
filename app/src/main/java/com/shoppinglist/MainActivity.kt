package com.shoppinglist

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay3.BehaviorRelay
import com.shoppinglist.databinding.ActivityMainBinding
import com.shoppinglist.databinding.DialogueAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    @Inject
    lateinit var db: AppDatabase
    private val shoppingList = BehaviorRelay.create<MutableList<ShoppingListItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = mutableListOf<ShoppingListItem>()
        list.add(ShoppingListItem(UUID.randomUUID().toString(), "a", false))
        list.add(ShoppingListItem(UUID.randomUUID().toString(), "b", true))
        list.add(ShoppingListItem(UUID.randomUUID().toString(), "c", false))
        list.add(ShoppingListItem(UUID.randomUUID().toString(), "d", true))
        shoppingList.accept(list)

        initView()
    }

    private fun initView() {
        val recyclerView = binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = MainRecyclerViewAdapter(this, shoppingList.value)
            setTouchGestureOnRecyclerView().attachToRecyclerView(it)
        }

        val fab = binding.fab.also {
            it.setOnClickListener {
                buildDialogue(this).show()
            }
        }
    }

    private fun setTouchGestureOnRecyclerView(): ItemTouchHelper {
        return ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.RIGHT) {
                        val position = viewHolder.adapterPosition
                        val value = shoppingList.value
                        value.remove(value[position])
                        shoppingList.accept(value)
                        recyclerView.adapter?.notifyItemRemoved(position)
                    }
                }
            })

    }

    private fun buildDialogue(activity: Activity): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity).also {
            val dialogueBinding = DialogueAddBinding.inflate(LayoutInflater.from(this))
            it.setView(dialogueBinding.root)
            it.setPositiveButton(R.string.ok) { _, _ ->
                val value = shoppingList.value
                val inputString = dialogueBinding.textInput.text.toString()
                if (inputString.isNotEmpty()) {
                    value.add(ShoppingListItem(UUID.randomUUID().toString(), inputString, false))
                    shoppingList.accept(value)
                }
            }
            it.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }

        return builder.create()
    }
}