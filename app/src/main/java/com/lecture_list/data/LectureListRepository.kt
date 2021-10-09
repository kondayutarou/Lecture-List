package com.lecture_list.data

import com.lecture_list.model.LectureListItem
import com.lecture_list.model.LectureProgressApiItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Interface to the data layer.
 */
interface LectureListRepository {
    fun getLectureList(): Single<List<LectureListItem>>

    fun getProgress(): Single<LectureProgressApiItem>

    fun saveLectureList(): Completable

    fun saveProgress(): Completable
}