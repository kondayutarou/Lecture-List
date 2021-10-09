package com.lecture_list.data.source.local

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface LectureListItemDao {
    @Query("SELECT * FROM lecturelistdb")
    fun getAll(): Single<List<LectureListDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg lectureList: LectureListDB): Maybe<Void>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<LectureListDB>): Completable

    @Delete
    fun delete(lectureList: LectureListDB): Maybe<Void>

    @Query("DELETE FROM lecturelistdb")
    fun deleteAll(): Completable

    @Update
    fun update(vararg lectureItem: LectureListDB): Completable
}
