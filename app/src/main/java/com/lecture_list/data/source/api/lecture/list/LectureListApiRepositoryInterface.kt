package com.lecture_list.data.source.api.lecture.list

import com.lecture_list.model.LectureListApiItem
import com.lecture_list.model.LectureListItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LectureListApiRepositoryInterface {
    fun fetchLectureListObservable(): Single<List<LectureListApiItem>>
}