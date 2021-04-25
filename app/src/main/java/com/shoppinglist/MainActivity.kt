package com.shoppinglist

import android.app.Activity
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
import com.jakewharton.rxrelay3.BehaviorRelay
import com.orhanobut.logger.Logger
import com.shoppinglist.databinding.ActivityMainBinding
import com.shoppinglist.databinding.DialogueAddBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
    private lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var db: AppDatabase
    private val shoppingList = BehaviorRelay.createDefault(mutableListOf<ShoppingListItem>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        db.shoppingListItemDao().getAll()
            .observeOn(Schedulers.computation())
            .subscribe { list ->
                shoppingList.accept(list as MutableList<ShoppingListItem>)
            }
            .addTo(compositeDisposable)
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
                        val value = shoppingList.value
                        deletedPosition = position
                        deletedItem = value[position]
                        value.remove(deletedItem)
                        shoppingList.accept(value)
                        recyclerView.adapter?.notifyItemRemoved(position)
                        deletedItem?.let { item ->
                            db.shoppingListItemDao().delete(item)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                    },
                                    { error ->
                                        Logger.d(error.localizedMessage)
                                    }
                                )
                                .addTo(compositeDisposable)
                            showSnackBar()
                        }
                    }
                }

                private fun showSnackBar() {
                    Snackbar.make(
                        recyclerView,
                        "${deletedItem?.name as CharSequence} ${resources.getString(R.string.removed)}",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.undo, View.OnClickListener {
                            val value = shoppingList.value
                            val position = deletedPosition ?: return@OnClickListener
                            val item = deletedItem ?: return@OnClickListener
                            value.add(position, item)
                            shoppingList.accept(value)
                            recyclerView.adapter?.notifyItemInserted(position)
                        })
                        .show()
                }
            })
    }

    private fun buildDialogue(activity: Activity): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity).also {
            val dialogueBinding = DialogueAddBinding.inflate(LayoutInflater.from(this))
            val dialogueDisposable: CompositeDisposable = CompositeDisposable()
            it.setView(dialogueBinding.root)
            it.setPositiveButton(R.string.ok) { dialogInterface, _ ->
                val value = shoppingList.value
                val inputString = dialogueBinding.textInput.text.toString()
                if (inputString.isNotEmpty()) {
                    val item = ShoppingListItem(UUID.randomUUID().toString(), inputString, false)
                    value.add(item)
                    shoppingList.accept(value)
                    db.shoppingListItemDao().insert(item)
                        .subscribeOn(Schedulers.computation())
                        .subscribe(
                            {
                                dialogInterface.dismiss()
                            },
                            { error ->
                                Logger.d(error.localizedMessage)
                            }
                        )
                        .addTo(dialogueDisposable)
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
        compositeDisposable.dispose()
    }
}