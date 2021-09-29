package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListItemApi
import io.reactivex.rxjava3.core.Observable

interface LectureListApiRepositoryInterface {
    fun fetchLectureListObservable(): Observable<List<LectureListItemApi>>
}