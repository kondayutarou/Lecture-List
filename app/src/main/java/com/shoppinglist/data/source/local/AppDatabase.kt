package com.shoppinglist.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shoppinglist.data.ShoppingListItem

@Database(entities = arrayOf(ShoppingListItem::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao
}
