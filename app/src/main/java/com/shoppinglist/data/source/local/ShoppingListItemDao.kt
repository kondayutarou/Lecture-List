package com.shoppinglist.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shoppinglist.data.ShoppingListItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

@Dao
interface ShoppingListItemDao {
    @Query("SELECT * FROM shoppinglistitem")
    fun getAll(): Observable<List<ShoppingListItem>>

    @Insert
    fun insert(vararg shoppingListItem: ShoppingListItem): Maybe<Void>

    @Insert
    fun insertAll(items: List<ShoppingListItem>): Completable

    @Delete
    fun delete(shoppingListItem: ShoppingListItem): Maybe<Void>

    @Query("DELETE FROM shoppinglistitem")
    fun deleteAll(): Completable
}
