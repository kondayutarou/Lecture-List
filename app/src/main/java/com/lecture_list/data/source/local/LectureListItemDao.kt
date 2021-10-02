package com.lecture_list.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lecture_list.data.LectureListDB
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

@Dao
interface LectureListItemDao {
    @Query("SELECT * FROM lecturelistdb")
    fun getAll(): Observable<List<LectureListDB>>

    @Insert
    fun insert(vararg lectureList: LectureListDB): Maybe<Void>

    @Insert
    fun insertAll(items: List<LectureListDB>): Completable

    @Delete
    fun delete(lectureList: LectureListDB): Maybe<Void>

    @Query("DELETE FROM lecturelistdb")
    fun deleteAll(): Completable
}
