package com.lecture_list.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lecture_list.data.ShoppingListItem

@Database(entities = arrayOf(ShoppingListItem::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao
}
