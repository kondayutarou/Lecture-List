package com.lecture_list.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lecture_list.data.LectureListDB

@Database(entities = arrayOf(LectureListDB::class), version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lectureListItemDao(): LectureListItemDao
}
