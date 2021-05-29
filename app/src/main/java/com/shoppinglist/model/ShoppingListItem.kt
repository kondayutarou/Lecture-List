package com.shoppinglist.model

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

interface ShoppingListItemInterface {
    val uuid: String
    val name: String
    var checked: Boolean
}

@Entity
data class ShoppingListItem(
    @PrimaryKey override val uuid: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "checked") override var checked: Boolean
) : ShoppingListItemInterface

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

@Database(entities = arrayOf(ShoppingListItem::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao
}
