package com.lecture_list.view

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.orhanobut.logger.Logger
import com.lecture_list.data.ShoppingListItem
import com.lecture_list.data.source.local.AppDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val db: AppDatabase
) : ViewModel() {

    val shoppingList: BehaviorRelay<MutableList<ShoppingListItem>> = BehaviorRelay.createDefault(mutableListOf())
    private lateinit var compositeDisposable: CompositeDisposable
    val insertItemSuccess: PublishRelay<Boolean> = PublishRelay.create()

    fun start() {
        compositeDisposable = CompositeDisposable()
        getAllItems()
    }

    private fun getAllItems() {
        db.shoppingListItemDao().getAll()
            .observeOn(Schedulers.computation())
            .subscribe { list ->
                shoppingList.accept(list as MutableList<ShoppingListItem>)
            }
            .addTo(compositeDisposable)
    }

//    fun insertItem(itemToInsert: ShoppingListItem) {
//        db.shoppingListItemDao().insert(itemToInsert)
//            .subscribeOn(Schedulers.computation())
//            .subscribe(
//                {
//                    insertItemSuccess.accept(true)
//                },
//                { error ->
//                    Logger.d(error.localizedMessage)
//                }
//            )
//            .addTo(compositeDisposable)
//    }

//    fun deleteItem(itemToDelete: ShoppingListItem) {
//        db.shoppingListItemDao().delete(itemToDelete)
//            .subscribeOn(Schedulers.computation())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                {
//                },
//                { error ->
//                    Logger.d(error.localizedMessage)
//                }
//            )
//            .addTo(compositeDisposable)
//    }

    fun saveAll() {
        db.shoppingListItemDao().deleteAll()
            .concatWith(db.shoppingListItemDao().insertAll(shoppingList.value))
            .subscribeOn(Schedulers.computation())
            .subscribe {
                Logger.d("Database rewrite complete")
            }
            .addTo(compositeDisposable)
    }

    fun finish() {
        compositeDisposable.clear()
    }
}