package com.lecture_list.data.source.local

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface LectureListItemDao {
    /**
     * Select an item by id.
     *
     * @param id the lecture id.
     * @return the lecture with id.
     */
    @Query("SELECT * FROM lecturelistdbitem WHERE id == :id")
    fun get(id: String): Single<LectureListDBItem>

    /**
     * Select all lectures.
     *
     * @return all lectures.
     */
    @Query("SELECT * FROM lecturelistdbitem")
    fun getAll(): Single<List<LectureListDBItem>>

    /**
     * Insert a lecture in the database. If the lecture already exists, replace it.
     *
     * @param lectureListItem the lecture to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg lectureListItem: LectureListDBItem): Single<Void>

    /**
     * Insert a list of lectures in the database. If the lectures already exist, replace them.
     *
     * @param items the list of lectures to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<LectureListDBItem>): Completable

    /**
     * Delete a lecture.
     *
     * @param lectureListItem the lecture to be deleted
     */
    @Delete
    fun delete(lectureListItem: LectureListDBItem): Single<Void>

    /**
     * Delete all lectures from the database.
     */
    @Query("DELETE FROM lecturelistdbitem")
    fun deleteAll(): Completable

    /**
     * Update the lecture.
     *
     * @param lectureItem lecture to be updated.
     */
    @Update
    fun update(vararg lectureItem: LectureListDBItem): Completable

    /**
     * Update the lecture's progress. Query by id.
     *
     * @param id id of lecture to be updated.
     * @param progress progress of updated lecture.
     */
    @Query("UPDATE lecturelistdbitem SET progress = :progress WHERE id = :id")
    fun updateById(id: String, progress: Int): Completable
}
