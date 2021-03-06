package com.lecture_list.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(LectureListDBItem::class), version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lectureListItemDao(): LectureListItemDao
}
