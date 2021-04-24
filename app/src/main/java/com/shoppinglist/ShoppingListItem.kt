package com.shoppinglist

import androidx.room.*
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
    fun getAll(): List<ShoppingListItem>

    @Insert
    fun insert(vararg shoppingListItem: ShoppingListItem)

    @Delete
    fun delete(shoppingListItem: ShoppingListItem)
}

@Database(entities = arrayOf(ShoppingListItem::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao
}
