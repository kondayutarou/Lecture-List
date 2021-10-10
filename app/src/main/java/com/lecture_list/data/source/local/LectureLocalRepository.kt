package com.lecture_list.data.source.local

import com.lecture_list.data.source.api.lecture.progress.LectureProgressApiItem
import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface LectureLocalRepository {
    fun saveList(list: List<LectureListItem>): Completable

    fun saveProgress(apiItem: LectureProgressApiItem): Completable

    fun loadList(): Single<List<LectureListDBItem>>
}