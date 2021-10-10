package com.lecture_list.data

import com.lecture_list.model.LectureListItem
import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiItem
import com.lecture_list.model.StoreValueWithApiError
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Interface to the data layer.
 */
interface LectureListRepository {
    fun getLectureList(): Single<StoreValueWithApiError<List<LectureListItem>>>

    fun getProgress(id: String): Single<StoreValueWithApiError<LectureProgressApiItem>>

    fun saveProgress(progressItem: LectureProgressApiItem): Completable

    fun saveLectureList(list: List<LectureListItem>): Completable

    fun loadLectureList(): Single<List<LectureListItem>>
}