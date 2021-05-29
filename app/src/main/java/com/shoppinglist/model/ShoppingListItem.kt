package com.shoppinglist

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import java.util.*

@Entity
data class ShoppingListItem(
    @PrimaryKey val uuid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "checked") val checked: Boolean
)

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
