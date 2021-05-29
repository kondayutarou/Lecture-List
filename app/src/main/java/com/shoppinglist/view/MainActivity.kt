package com.shoppinglist.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shoppinglist.R
import com.shoppinglist.ShoppingListItem
import com.shoppinglist.databinding.ActivityMainBinding
import com.shoppinglist.databinding.DialogueAddBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
    @Inject
    lateinit var viewModel: MainViewModel
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
        initView()
    }

    private fun initView() {
        binding.viewModel = viewModel
        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = MainRecyclerViewAdapter(this@MainActivity, viewModel.shoppingList)
            setTouchGestureOnRecyclerView().attachToRecyclerView(this)
        }

        binding.fabAdd.apply {
            this.setOnClickListener {
                buildDialogue(this@MainActivity).show()
            }
        }

        observeItemChange()
    }

    private fun observeItemChange() {
        viewModel.shoppingList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.recyclerView.adapter?.notifyDataSetChanged()
            }
            .addTo(compositeDisposable)
    }

    private fun setTouchGestureOnRecyclerView(): ItemTouchHelper {
        return ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT
            ) {
                private var deletedItem: ShoppingListItem? = null
                private var deletedPosition: Int? = null
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
                        val value = viewModel.shoppingList.value
                        deletedPosition = position
                        deletedItem = value[position]
                        value.remove(deletedItem)
                        viewModel.shoppingList.accept(value)
                        deletedItem?.let { item ->
                            showSnackBar()
                        }
                    }
                }

                private fun showSnackBar() {
                    Snackbar.make(
                        binding.recyclerView,
                        "${deletedItem?.name as CharSequence} ${getString(R.string.removed)}",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.undo, View.OnClickListener {
                            val value = viewModel.shoppingList.value
                            val position = deletedPosition ?: return@OnClickListener
                            val item = deletedItem ?: return@OnClickListener
                            value.add(position, item)
                            viewModel.shoppingList.accept(value)
                        })
                        .show()
                }
            })
    }

    private fun buildDialogue(context: Context): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context).also {
            val dialogueBinding = DialogueAddBinding.inflate(LayoutInflater.from(context))
            val dialogueDisposable: CompositeDisposable = CompositeDisposable()
            it.setView(dialogueBinding.root)
            it.setPositiveButton(R.string.ok) { dialogInterface, _ ->
                val value = viewModel.shoppingList.value
                val inputString = dialogueBinding.textInput.text.toString()
                if (inputString.isNotEmpty()) {
                    val item = ShoppingListItem(UUID.randomUUID().toString(), inputString, false)
                    value.add(item)
                    viewModel.shoppingList.accept(value)
                }
            }
            it.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            it.setOnDismissListener {
                dialogueDisposable.dispose()
            }
        }

        return builder.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.finish()
        compositeDisposable.clear()
    }
}