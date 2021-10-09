package com.lecture_list.data.source.local

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface LectureListItemDao {
    @Query("SELECT * FROM lecturelistdbitem WHERE id == :id")
    fun get(id: String): Single<LectureListDBItem>

    @Query("SELECT * FROM lecturelistdbitem")
    fun getAll(): Single<List<LectureListDBItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg lectureListItem: LectureListDBItem): Single<Void>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<LectureListDBItem>): Completable

    @Delete
    fun delete(lectureListItem: LectureListDBItem): Single<Void>

    @Query("DELETE FROM lecturelistdbitem")
    fun deleteAll(): Completable

    @Update
    fun update(vararg lectureItem: LectureListDBItem): Completable
}
