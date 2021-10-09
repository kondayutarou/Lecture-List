package com.lecture_list.data.source.api.lecture.list

import io.reactivex.rxjava3.core.Single

interface LectureListRemoteRepository {
    fun fetchLectureListObservable(): Single<List<LectureListApiItem>>
}